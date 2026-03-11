package com.codeintellix.envlink.data.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.theme.DarkBlue
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.OrangeYellow
import com.codeintellix.envlink.activity.theme.SkyBlue
import com.codeintellix.envlink.activity.theme.Yellow
import com.codeintellix.envlink.activity.theme.YellowGreen
import com.codeintellix.envlink.domain.protocol.BleProtocolHelper
import com.codeintellix.envlink.domain.sensor.SensorDataHelper
import com.codeintellix.envlink.entity.actuator.ActuatorState
import com.codeintellix.envlink.entity.actuator.ActuatorStatus
import com.codeintellix.envlink.entity.actuator.ActuatorType
import com.codeintellix.envlink.entity.device.BleUuid
import com.codeintellix.envlink.entity.device.ConnectionState
import com.codeintellix.envlink.entity.device.Device
import com.codeintellix.envlink.entity.protocol.CalibrationEvent
import com.codeintellix.envlink.entity.protocol.CalibrationType
import com.codeintellix.envlink.entity.protocol.CommandType
import com.codeintellix.envlink.entity.protocol.ControlMode
import com.codeintellix.envlink.entity.protocol.ControlParams
import com.codeintellix.envlink.entity.protocol.SystemInfo
import com.codeintellix.envlink.entity.sensor.SensorData
import com.codeintellix.envlink.entity.sensor.SensorDataVO
import com.codeintellix.envlink.entity.sensor.SensorStatus
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
class DeviceDetailViewModel(
    private val deviceAddress: String,
    context: Context
) : ViewModel() {
    private val appContext: Context = context.applicationContext
    private val repository = DeviceRepository.getInstance(appContext)
    private val gson = Gson()
    private var gatt: BluetoothGatt? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null

    private var lastGetSensorTime = 0L // 上次发送获取传感器指令的时间戳
    private var lastGetActuatorTime = 0L // 上次发送获取执行器状态指令的时间戳
    private var lastGetParamsTime = 0L
    private var isPollingActive = false // 定时任务是否在运行
    private var pendingActuatorType: ActuatorType? = null // 记录最近一次操作（用于对比结果）
    private var pendingTargetState: ActuatorState? = null
    private var pendingCalibrationType: CalibrationType? = null
    private var pendingParamsSave: Boolean = false

    companion object {
        private const val MIN_REFRESH_INTERVAL = 5000L
        private const val AUTO_REFRESH_INTERVAL = 30000L
    }

    private val _toastMessage = Channel<String>(Channel.BUFFERED)
    val toastMessage: Flow<String> = _toastMessage.receiveAsFlow()

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device.asStateFlow()
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    private val _receivedData = MutableStateFlow("")
    val receivedData: StateFlow<String> = _receivedData.asStateFlow()

    // 系统
    private val _systemInfo = MutableStateFlow<SystemInfo?>(null)
    val systemInfo: StateFlow<SystemInfo?> = _systemInfo.asStateFlow()

    // 数据
    private val _sensorDataList = MutableStateFlow(getDefaultSensorDataVO())
    val sensorDataList: StateFlow<List<SensorDataVO>> = _sensorDataList.asStateFlow()

    // 控制
    private val _controlMode = MutableStateFlow(ControlMode.AUTO) // 默认为手动
    val controlMode: StateFlow<ControlMode> = _controlMode.asStateFlow()
    private val _pumpState = MutableStateFlow(ActuatorState.OFF)
    val pumpState: StateFlow<ActuatorState> = _pumpState.asStateFlow()
    private val _fanState = MutableStateFlow(ActuatorState.OFF)
    val fanState: StateFlow<ActuatorState> = _fanState.asStateFlow()
    private val _lightState = MutableStateFlow(ActuatorState.OFF)
    val lightState: StateFlow<ActuatorState> = _lightState.asStateFlow()
    private val _pumpOperationLoading = MutableStateFlow(false)
    val pumpOperationLoading: StateFlow<Boolean> = _pumpOperationLoading.asStateFlow()
    private val _fanOperationLoading = MutableStateFlow(false)
    val fanOperationLoading: StateFlow<Boolean> = _fanOperationLoading.asStateFlow()
    private val _lightOperationLoading = MutableStateFlow(false)
    val lightOperationLoading: StateFlow<Boolean> = _lightOperationLoading.asStateFlow()

    // 参数
    private val _controlParams = MutableStateFlow(ControlParams.DEFAULT)
    val controlParams: StateFlow<ControlParams> = _controlParams.asStateFlow()
    private val _draftParams = MutableStateFlow(ControlParams.DEFAULT)
    val draftParams: StateFlow<ControlParams> = _draftParams.asStateFlow() // 临时保存用户修改的草稿
    private val _paramsLoading = MutableStateFlow(false)
    val paramsLoading: StateFlow<Boolean> = _paramsLoading.asStateFlow()
    private val _isParamsChanged = MutableStateFlow(false)  // 界面参数是否有未保存修改
    val isParamsChanged: StateFlow<Boolean> = _isParamsChanged.asStateFlow()

    // 校准
    private val _calibrationLoading = MutableStateFlow(false)
    val calibrationLoading: StateFlow<Boolean> = _calibrationLoading.asStateFlow()
    private val _calibrationEvents = MutableSharedFlow<CalibrationEvent>()
    val calibrationEvents: SharedFlow<CalibrationEvent> = _calibrationEvents.asSharedFlow()

    init {
        // 初始化时从 Device 实体加载上次传感器数据
        loadLatestSensorDataVO()
        connect()
    }

    private fun getDefaultSensorDataVO(): List<SensorDataVO> {
        return listOf(
            SensorDataVO(
                title = "环境温度",
                value = "--",
                unit = "℃",
                status = SensorStatus.UNKNOWN,
                progress = 0.5f,
                icon = R.drawable.ic_thermometer,
                iconColor = listOf(LightGreen, YellowGreen)
            ),
            SensorDataVO(
                title = "空气湿度",
                value = "--",
                unit = "%",
                status = SensorStatus.UNKNOWN,
                progress = 0.5f,
                icon = R.drawable.ic_water,
                iconColor = listOf(SkyBlue, DarkBlue)
            ),
            SensorDataVO(
                title = "光照强度",
                value = "--",
                unit = "Lux",
                status = SensorStatus.UNKNOWN,
                progress = 0.5f,
                icon = R.drawable.ic_sunny,
                iconColor = listOf(Yellow, OrangeYellow)
            ),
            SensorDataVO(
                title = "土壤湿度",
                value = "--",
                unit = "%",
                status = SensorStatus.UNKNOWN,
                progress = 0.5f,
                icon = R.drawable.ic_moisture,
                iconColor = listOf(OrangeYellow, OrangeRed)
            )
        )
    }

    private fun loadLatestSensorDataVO() {
        viewModelScope.launch {
            repository.getAllDevices().collect { devices ->
                val device = devices.find { it.address == deviceAddress }
                _device.value = device
                device?.latestSensorData?.let { json ->
                    try {
                        val sensorData = gson.fromJson(
                            json,
                            SensorData::class.java
                        )
                        updateSensorDataVO(sensorData, 0xFF) // 假设所有数据有效
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

    private fun updateSensorDataVO(
        sensorData: SensorData,
        statusFlags: Int
    ) {
        val list = listOf(
            SensorDataVO(
                title = "环境温度",
                value = sensorData.temperature.toInt().toString(),
                unit = "℃",
                status = if ((statusFlags and 0x02) != 0) SensorDataHelper.getTemperatureStatus(
                    sensorData.temperature
                ) else SensorStatus.INVALID,
                progress = SensorDataHelper.mapTemperatureToProgress(sensorData.temperature),
                icon = R.drawable.ic_thermometer,
                iconColor = listOf(LightGreen, YellowGreen)
            ),
            SensorDataVO(
                title = "空气湿度",
                value = sensorData.humidity.toInt().toString(),
                unit = "%",
                status = if ((statusFlags and 0x04) != 0) SensorDataHelper.getHumidityStatus(
                    sensorData.humidity
                ) else SensorStatus.INVALID,
                progress = SensorDataHelper.mapHumidityToProgress(sensorData.humidity),
                icon = R.drawable.ic_water,
                iconColor = listOf(SkyBlue, DarkBlue)
            ),
            SensorDataVO(
                title = "光照强度",
                value = sensorData.lightIntensity.toString(),
                unit = "Lux",
                status = if ((statusFlags and 0x08) != 0) SensorDataHelper.getLightStatus(sensorData.lightIntensity.toFloat()) else SensorStatus.INVALID,
                progress = SensorDataHelper.mapLightToProgress(sensorData.lightIntensity.toFloat()),
                icon = R.drawable.ic_sunny,
                iconColor = listOf(Yellow, OrangeYellow)
            ),
            SensorDataVO(
                title = "土壤湿度",
                value = sensorData.soilMoisture.toInt().toString(),
                unit = "%",
                status = if ((statusFlags and 0x01) != 0) SensorDataHelper.getSoilMoistureStatus(
                    sensorData.soilMoisture
                ) else SensorStatus.INVALID,
                progress = SensorDataHelper.mapSoilMoistureToProgress(sensorData.soilMoisture),
                icon = R.drawable.ic_moisture,
                iconColor = listOf(OrangeYellow, OrangeRed)
            )
        )
        _sensorDataList.value = list
    }

    private fun updateSensorData(sensorData: SensorData) {
        val entitySensorData = SensorData(
            soilMoisture = sensorData.soilMoisture,
            temperature = sensorData.temperature,
            humidity = sensorData.humidity,
            lightIntensity = sensorData.lightIntensity
        )
        val json = gson.toJson(entitySensorData)

        viewModelScope.launch {
            _device.value?.let {
                repository.updateSensorData(
                    address = deviceAddress,
                    sensorData = json,
                    lastConnectedTime = System.currentTimeMillis()
                )
            }
        }

        sensorData.statusFlags?.let { updateSensorDataVO(entitySensorData, it) }
    }

    private fun updateActuatorStatus(status: ActuatorStatus) {
        _pumpState.value = status.pump
        _fanState.value = status.fan
        _lightState.value = status.light

        pendingActuatorType?.let { id ->
            pendingTargetState?.let { targetState ->
                val actualState = when (id) {
                    ActuatorType.PUMP -> status.pump
                    ActuatorType.FAN -> status.fan
                    ActuatorType.LIGHT -> status.light
                }
                val success = actualState == targetState && actualState != ActuatorState.ERROR
                updateToastMessage(if (success) "操作成功" else "操作失败")
                pendingActuatorType = null
                pendingTargetState = null
            }
        }

        _pumpOperationLoading.value = false
        _fanOperationLoading.value = false
        _lightOperationLoading.value = false
    }

    private fun updateToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.send(message)
        }
    }

    @SuppressLint("MissingPermission")
    private fun connect() {
        val bluetoothAdapter =
            (appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
        val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        if (device == null) {
            _connectionState.value = ConnectionState.Failed("无效的设备地址")
            updateToastMessage("设备连接失败：无效的地址")
            return
        }
        _connectionState.value = ConnectionState.Connecting

        val callback = object : BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        gatt.discoverServices()
                    }

                    BluetoothProfile.STATE_DISCONNECTED -> {
                        _connectionState.value = ConnectionState.Disconnected
                        gatt.close()
                        this@DeviceDetailViewModel.gatt = null
                        writeCharacteristic = null
                        notifyCharacteristic = null
                        stopPolling()
                    }
                }
            }

            @SuppressLint("MissingPermission")
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt.getService(BleUuid.SERVICE_UUID)
                    if (service != null) {
                        writeCharacteristic = service.getCharacteristic(BleUuid.TX_CHAR_UUID)
                        val rxChar = service.getCharacteristic(BleUuid.RX_CHAR_UUID)
                        if (rxChar != null) {
                            gatt.setCharacteristicNotification(rxChar, true)
                            val descriptor = rxChar.getDescriptor(BleUuid.CCCD_UUID)
                            descriptor?.let {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    gatt.writeDescriptor(
                                        it,
                                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                    )
                                } else {
                                    @Suppress("DEPRECATION")
                                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                    @Suppress("DEPRECATION")
                                    gatt.writeDescriptor(it)
                                }
                            }
                            notifyCharacteristic = rxChar
                        }
                        _connectionState.value = ConnectionState.Connected(gatt.device)

                        // 连接成功后加载信息状态
                        startPolling()
                        viewModelScope.launch {
                            delay(300)
                            fetchSystemInfo()
                            delay(300)
                            fetchActuatorState()
                            delay(300)
                            fetchControlParams()
                        }
                    } else {
                        _connectionState.value = ConnectionState.Failed("未找到指定服务")
                        updateToastMessage("设备连接失败：未找到指定服务")
                    }
                } else {
                    _connectionState.value = ConnectionState.Failed("服务发现失败")
                    updateToastMessage("设备连接失败：服务发现失败")
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                handleReceivedData(value)
            }

            @Suppress("DEPRECATION")
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                val value = characteristic.value
                if (value != null) handleReceivedData(value)
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    handleCommandError("发送失败")
                    _connectionState.value = ConnectionState.Failed("发送失败")
                }
            }
        }

        gatt = device.connectGatt(appContext, false, callback, BluetoothDevice.TRANSPORT_LE)
    }

    /**
     * 处理接响应包
     */
    private fun handleReceivedData(data: ByteArray) {
        // 记录原始数据（调试）
        _receivedData.value = data.joinToString(" ") { "%02X".format(it) }

        val parsed = BleProtocolHelper.parseResponse(data) ?: run {
            Log.e("DeviceDetail", "Invalid packet")
            return
        }

        when (parsed.command) {
            CommandType.ACK.value -> {
                when (parsed.payload.size) {
                    13 -> { // 传感器数据
                        BleProtocolHelper.parseSensorData(parsed.payload)?.let { sensorData ->
                            updateSensorData(sensorData)
                        }
                    }

                    3 -> { // 执行器状态
                        BleProtocolHelper.parseActuatorStatus(parsed.payload)?.let { status ->
                            updateActuatorStatus(status)
                        }
                    }

                    10, 12 -> { // 系统信息
                        BleProtocolHelper.parseSystemInfo(parsed.payload)?.let { info ->
                            _controlMode.value = info.controlMode
                            _systemInfo.value = info
                        }
                    }

                    32 -> { // 控制参数
                        BleProtocolHelper.parseControlParams(parsed.payload)?.let { params ->
                            _controlParams.value = params
                            _draftParams.value = params // 同步草稿
                            _isParamsChanged.value = false
                            _paramsLoading.value = false
                        }
                    }

                    0 -> {
                        // 设置类命令的成功响应
                        if (pendingParamsSave) {
                            pendingParamsSave = false
                            updateToastMessage("参数保存成功")
                        }
                    }

                    else -> Log.w("DeviceDetail", "未知负载长度：${parsed.payload.size}")
                }

                if (pendingCalibrationType != null) {
                    val type = pendingCalibrationType   // 先保存到局部变量
                    pendingCalibrationType = null       // 立即清空，避免并发问题
                    _calibrationLoading.value = false
                    viewModelScope.launch {
                        _calibrationEvents.emit(CalibrationEvent(type!!, true))
                    }
                }
            }

            CommandType.ERROR.value -> {
                if (pendingParamsSave) {
                    pendingParamsSave = false
                    _paramsLoading.value = false
                    updateToastMessage("参数保存失败")
                } else if (pendingCalibrationType != null) {
                    val type = pendingCalibrationType
                    pendingCalibrationType = null
                    _calibrationLoading.value = false
                    viewModelScope.launch {
                        _calibrationEvents.emit(CalibrationEvent(type!!, false))
                    }
                } else if (pendingActuatorType != null) {
                    pendingActuatorType = null
                    pendingTargetState = null
                    _pumpOperationLoading.value = false
                    _fanOperationLoading.value = false
                    _lightOperationLoading.value = false
                    updateToastMessage("操作失败")
                } else {
                    _connectionState.value = ConnectionState.Failed("设备返回错误")
                    updateToastMessage("操作失败")
                }
            }

            else -> {
                Log.w("DeviceDetail", "未知命令：${parsed.command}")
            }
        }
    }

    private fun handleCommandError(errorMessage: String = "操作失败") {
        var handled = false
        if (pendingParamsSave) {
            pendingParamsSave = false
            _paramsLoading.value = false
            updateToastMessage("参数保存失败")
            handled = true
        }
        if (pendingCalibrationType != null) {
            val type = pendingCalibrationType
            pendingCalibrationType = null
            _calibrationLoading.value = false
            viewModelScope.launch {
                _calibrationEvents.emit(CalibrationEvent(type!!, false))
            }
            handled = true
        }
        if (pendingActuatorType != null) {
            pendingActuatorType = null
            pendingTargetState = null
            _pumpOperationLoading.value = false
            _fanOperationLoading.value = false
            _lightOperationLoading.value = false
            updateToastMessage(errorMessage)
            handled = true
        }
        if (!handled) {
            updateToastMessage(errorMessage)
        }
    }

    private fun canSendCommand(): Boolean = gatt != null && writeCharacteristic != null

    fun fetchSensorData() {
        if (!canSendCommand()) return
        val now = System.currentTimeMillis()
        if (now - lastGetSensorTime < MIN_REFRESH_INTERVAL) return
        lastGetSensorTime = now
        sendCommand(BleProtocolHelper.buildGetSensorDataCommand())
    }

    fun fetchActuatorState(force: Boolean = false) {
        if (!canSendCommand()) {
            if (pendingActuatorType != null) {
                pendingActuatorType = null
                pendingTargetState = null
                _pumpOperationLoading.value = false
                _fanOperationLoading.value = false
                _lightOperationLoading.value = false
                updateToastMessage("获取状态失败")
            }
            return
        }
        val now = System.currentTimeMillis()
        if (!force && now - lastGetActuatorTime < MIN_REFRESH_INTERVAL) return
        lastGetActuatorTime = now
        sendCommand(BleProtocolHelper.buildGetActuatorStateCommand())
    }

    fun fetchSystemInfo() {
        if (!canSendCommand()) return
        sendCommand(BleProtocolHelper.buildGetSystemInfoCommand())
    }

    fun fetchControlParams(force: Boolean = false) {
        if (!canSendCommand()) {
            // 如果是因为保存操作触发的刷新，立即清理
            if (pendingParamsSave) {
                pendingParamsSave = false
                _paramsLoading.value = false
                updateToastMessage("参数获取失败")
            }
            return
        }
        val now = System.currentTimeMillis()
        if (!force && now - lastGetParamsTime < MIN_REFRESH_INTERVAL) return
        lastGetParamsTime = now
        _paramsLoading.value = true
        sendCommand(BleProtocolHelper.buildGetControlParamsCommand())

        // 超时保护：2 秒后若 loading 仍为 true 则自动重置
        viewModelScope.launch {
            delay(2000)
            if (_paramsLoading.value) {
                _paramsLoading.value = false
                if (pendingParamsSave) {
                    pendingParamsSave = false
                    updateToastMessage("参数保存超时")
                } else {
                    updateToastMessage("获取参数超时")
                }
            }
        }
    }

    fun setControlMode(mode: ControlMode) {
        if (!canSendCommand()) {
            updateToastMessage("设备未连接")
            return
        }
        _controlMode.value = mode
        sendCommand(BleProtocolHelper.buildSetControlModeCommand(mode.value))
        viewModelScope.launch {
            delay(300)
            fetchSystemInfo()
        }
    }

    fun setActuator(actuatorType: ActuatorType, state: ActuatorState) {
        if (!canSendCommand()) {
            updateToastMessage("设备未连接")
            return
        }
        when (actuatorType) {
            ActuatorType.PUMP -> _pumpOperationLoading.value = true
            ActuatorType.FAN -> _fanOperationLoading.value = true
            ActuatorType.LIGHT -> _lightOperationLoading.value = true
        }
        pendingActuatorType = actuatorType
        pendingTargetState = state
        sendCommand(BleProtocolHelper.buildSetActuatorCommand(actuatorType.value, state.value))
        viewModelScope.launch {
            delay(300)
            fetchActuatorState(force = true)
        }
    }

    fun updateDraftParams(newParams: ControlParams) {
        // 对每个字段进行边界裁剪
        val safeParams = newParams.copy(
            soilMoistureLow = newParams.soilMoistureLow.coerceIn(0f, 100f),
            soilMoistureHigh = newParams.soilMoistureHigh.coerceIn(0f, 100f),
            temperatureLow = newParams.temperatureLow.coerceIn(-50f, 150f),
            temperatureHigh = newParams.temperatureHigh.coerceIn(-50f, 150f),
            lightIntensityLow = newParams.lightIntensityLow.coerceIn(0f, 50000f),
            lightIntensityHigh = newParams.lightIntensityHigh.coerceIn(0f, 50000f),
            minPumpInterval = newParams.minPumpInterval.coerceIn(0, 86400),
            maxPumpDuration = newParams.maxPumpDuration.coerceIn(0, 3600)
        )
        _draftParams.value = safeParams
        _isParamsChanged.value = safeParams != _controlParams.value
    }

    fun saveControlParams() {
        if (!canSendCommand()) {
            updateToastMessage("设备未连接")
            return
        }
        if (!_isParamsChanged.value) return

        viewModelScope.launch {
            _paramsLoading.value = true
            pendingParamsSave = true
            sendCommand(BleProtocolHelper.buildSetControlParamsCommand(_draftParams.value))
            delay(300)
            fetchControlParams(force = true)
        }
    }

    fun calibrate(type: CalibrationType) {
        if (!canSendCommand()) {
            viewModelScope.launch { _toastMessage.send("设备未连接") }
            return
        }
        pendingCalibrationType = type
        _calibrationLoading.value = true
        sendCommand(BleProtocolHelper.buildCalibrateCommand(type))
    }

    fun refresh() {
        fetchSensorData()
        fetchActuatorState()
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        stopPolling()
        gatt?.disconnect()
        gatt?.close()
        gatt = null
        writeCharacteristic = null
        notifyCharacteristic = null
        _connectionState.value = ConnectionState.Disconnected
    }

    @SuppressLint("MissingPermission")
    private fun sendCommand(command: ByteArray) {
        val gatt = this.gatt ?: return
        val char = writeCharacteristic ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt.writeCharacteristic(
                    char,
                    command,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            } else {
                @Suppress("DEPRECATION")
                char.value = command
                @Suppress("DEPRECATION")
                gatt.writeCharacteristic(char)
            }
        } catch (_: Exception) {
            handleCommandError("发送命令失败")
            _connectionState.value = ConnectionState.Failed("发送异常")
        }
    }

    private fun startPolling() {
        if (isPollingActive) return
        isPollingActive = true
        viewModelScope.launch {
            while (isPollingActive) {
                fetchSensorData()
                delay(300)
                fetchSystemInfo()
                delay(AUTO_REFRESH_INTERVAL - 300)
            }
        }
    }

    private fun stopPolling() {
        isPollingActive = false
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}