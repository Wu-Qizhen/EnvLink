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
import com.codeintellix.envlink.domain.sensor.SensorDataUtil
import com.codeintellix.envlink.entity.device.ConnectionState
import com.codeintellix.envlink.entity.device.Device
import com.codeintellix.envlink.entity.protocol.CommandType
import com.codeintellix.envlink.entity.sensor.SensorData
import com.codeintellix.envlink.entity.sensor.SensorDataVO
import com.codeintellix.envlink.entity.sensor.SensorStatus
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

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

    // BLE 相关常量（与 BT24 模块匹配）
    private val serviceUuid = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
    private val txCharUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")  // 写特征
    private val rxCharUuid =
        UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")  // 通知特征（通常与写相同）
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")

    // 执行器 ID 常量
    companion object {
        const val ACTUATOR_PUMP = 0      // 水泵
        const val ACTUATOR_FAN = 1       // 风扇
        const val ACTUATOR_LIGHT = 2     // 补光灯

        private const val MIN_REFRESH_INTERVAL = 5000L
        private const val AUTO_REFRESH_INTERVAL = 30000L
    }

    // 设备信息
    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device.asStateFlow()

    // 连接状态
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    // 传感器数据列表（用于 UI 展示）
    private val _sensorDataList = MutableStateFlow(getDefaultSensorData())
    val sensorDataList: StateFlow<List<SensorDataVO>> = _sensorDataList.asStateFlow()

    // 执行器状态（0 = OFF，1 = ON，2 = ERROR）
    private val _pumpState = MutableStateFlow(0)
    val pumpState: StateFlow<Int> = _pumpState.asStateFlow()

    private val _fanState = MutableStateFlow(0)
    val fanState: StateFlow<Int> = _fanState.asStateFlow()

    private val _lightState = MutableStateFlow(0)
    val lightState: StateFlow<Int> = _lightState.asStateFlow()

    // 原始接收数据（调试用）
    private val _receivedData = MutableStateFlow("")
    val receivedData: StateFlow<String> = _receivedData.asStateFlow()

    private var gatt: BluetoothGatt? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null

    // 上次发送获取传感器指令的时间戳（用于限制间隔 >=5 秒）
    private var lastGetSensorTime = 0L

    // 上次发送获取执行器状态指令的时间戳
    private var lastGetActuatorTime = 0L

    // 定时任务是否在运行
    private var isPollingActive = false

    init {
        // 初始化时从 Device 实体加载最新传感器数据
        loadLatestSensorData()
        connect()
    }

    // 获取默认传感器数据，确保 UI 一直展示
    private fun getDefaultSensorData(): List<SensorDataVO> {
        return listOf(
            SensorDataVO(
                title = "环境温度",
                value = "--",
                unit = "℃",
                status = "未知",
                statusColor = OrangeRed,
                progress = 0.5f,
                icon = R.drawable.ic_thermometer,
                iconColor = listOf(LightGreen, YellowGreen)
            ),
            SensorDataVO(
                title = "空气湿度",
                value = "--",
                unit = "%",
                status = "未知",
                statusColor = OrangeRed,
                progress = 0.5f,
                icon = R.drawable.ic_water,
                iconColor = listOf(SkyBlue, DarkBlue)
            ),
            SensorDataVO(
                title = "光照强度",
                value = "--",
                unit = "Lux",
                status = "未知",
                statusColor = OrangeRed,
                progress = 0.5f,
                icon = R.drawable.ic_sunny,
                iconColor = listOf(Yellow, OrangeYellow)
            ),
            SensorDataVO(
                title = "土壤湿度",
                value = "--",
                unit = "%",
                status = "未知",
                statusColor = OrangeRed,
                progress = 0.5f,
                icon = R.drawable.ic_moisture,
                iconColor = listOf(OrangeYellow, OrangeRed)
            )
        )
    }

    // 从 Device 实体加载最新传感器数据和设备信息
    private fun loadLatestSensorData() {
        viewModelScope.launch {
            try {
                // 从数据库获取设备信息
                repository.getAllDevices().collect { devices ->
                    val device = devices.find { it.address == deviceAddress }
                    if (device != null) {
                        // 更新设备信息
                        _device.value = device

                        // 如果有最新传感器数据，解析并更新 UI
                        if (device.latestSensorData.isNotEmpty()) {
                            // 解析 JSON 字符串为 SensorData
                            val sensorData =
                                gson.fromJson(device.latestSensorData, SensorData::class.java)
                            // 转换为 SensorDataVO 列表并更新状态
                            val sensorDataVOList = listOf(
                                SensorDataVO(
                                    title = "环境温度",
                                    value = sensorData.temperature.toInt().toString(),
                                    unit = "℃",
                                    status = "正常",
                                    statusColor = LightGreen,
                                    progress = sensorData.temperature / 50f,
                                    icon = R.drawable.ic_thermometer,
                                    iconColor = listOf(LightGreen, YellowGreen)
                                ),
                                SensorDataVO(
                                    title = "空气湿度",
                                    value = sensorData.humidity.toInt().toString(),
                                    unit = "%",
                                    status = "正常",
                                    statusColor = LightGreen,
                                    progress = sensorData.humidity / 100f,
                                    icon = R.drawable.ic_water,
                                    iconColor = listOf(SkyBlue, DarkBlue)
                                ),
                                SensorDataVO(
                                    title = "光照强度",
                                    value = sensorData.lightIntensity.toString(),
                                    unit = "Lux",
                                    status = "正常",
                                    statusColor = LightGreen,
                                    progress = sensorData.lightIntensity.toFloat() / 2000f,
                                    icon = R.drawable.ic_sunny,
                                    iconColor = listOf(Yellow, OrangeYellow)
                                ),
                                SensorDataVO(
                                    title = "土壤湿度",
                                    value = sensorData.soilMoisture.toInt().toString(),
                                    unit = "%",
                                    status = "正常",
                                    statusColor = LightGreen,
                                    progress = sensorData.soilMoisture / 100f,
                                    icon = R.drawable.ic_moisture,
                                    iconColor = listOf(OrangeYellow, OrangeRed)
                                )
                            )
                            _sensorDataList.value = sensorDataVOList
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DeviceDetail", "加载最新传感器数据失败", e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connect() {
        val bluetoothAdapter =
            (appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
        val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        if (device == null) {
            _connectionState.value = ConnectionState.Failed("无效的设备地址")
            return
        }
        _connectionState.value = ConnectionState.Connecting

        // 在 connect 方法内创建 callback，确保非空
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
                    val service = gatt.getService(serviceUuid)
                    if (service != null) {
                        writeCharacteristic = service.getCharacteristic(txCharUuid)
                        val rxChar = service.getCharacteristic(rxCharUuid)
                        if (rxChar != null) {
                            gatt.setCharacteristicNotification(rxChar, true)
                            val descriptor = rxChar.getDescriptor(cccdUuid)
                            descriptor?.let {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
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
                        startPolling()
                        // 连接成功后立即获取一次执行器状态
                        fetchActuatorState()
                    } else {
                        _connectionState.value = ConnectionState.Failed("未找到指定服务")
                    }
                } else {
                    _connectionState.value = ConnectionState.Failed("服务发现失败")
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
                    _connectionState.value = ConnectionState.Failed("发送失败")
                }
            }
        }

        gatt = device.connectGatt(appContext, false, callback, BluetoothDevice.TRANSPORT_LE)
    }

    /**
     * 处理接收到的数据（响应包）
     */
    private fun handleReceivedData(data: ByteArray) {
        // 记录原始数据（调试）
        _receivedData.value = data.joinToString(" ") { "%02X".format(it) }

        val packet = parseResponsePacket(data) ?: run {
            Log.e("DeviceDetail", "Invalid packet")
            return
        }

        when (packet.command) {
            CommandType.CMD_ACK.cmdByte -> {
                // 根据数据长度判断是哪种命令的响应
                when (packet.data.size) {
                    13 -> parseSensorData(packet.data)      // CMD_GET_SENSOR_DATA
                    3 -> parseActuatorState(packet.data)    // CMD_GET_ACTUATOR_STATE
                    0 -> {
                        // CMD_SET_ACTUATOR 的成功响应无数据，可以重新获取一次状态
                        fetchActuatorState()
                    }

                    else -> {
                        Log.w("DeviceDetail", "未知的 ACK 数据长度：${packet.data.size}")
                    }
                }
            }

            CommandType.CMD_ERROR.cmdByte -> {
                // 错误响应，可显示错误码
                _connectionState.value = ConnectionState.Failed("设备返回错误")
            }

            else -> {
                Log.w("DeviceDetail", "未知命令：${packet.command}")
            }
        }
    }

    /**
     * 解析响应包
     */
    private fun parseResponsePacket(data: ByteArray): ResponsePacket? {
        if (data.size < 5) return null
        if (data[0] != 0xAA.toByte() || data[data.size - 1] != 0x55.toByte()) return null
        val cmd = data[1].toInt() and 0xFF
        val len = data[2].toInt() and 0xFF
        if (data.size != 5 + len) return null
        // 可在此验证校验和，省略
        val payload = data.sliceArray(3 until 3 + len)
        return ResponsePacket(cmd, payload)
    }

    private data class ResponsePacket(val command: Int, val data: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ResponsePacket
            if (command != other.command) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            var result = command
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    /**
     * 解析传感器数据 CompactSensorData（13 字节）
     */
    private fun parseSensorData(data: ByteArray) {
        if (data.size < 13) return

        // 小端解析
        val soilMoistureRaw = (data[1].toInt() and 0xFF shl 8) or (data[0].toInt() and 0xFF)
        val temperatureRaw = (data[3].toInt() and 0xFF shl 8) or (data[2].toInt() and 0xFF)
        val humidityRaw = (data[5].toInt() and 0xFF shl 8) or (data[4].toInt() and 0xFF)
        val lightRaw = (data[7].toInt() and 0xFF shl 8) or (data[6].toInt() and 0xFF)
        val status = data[8].toInt() and 0xFF

        // 转换为实际值
        val soilMoisture = soilMoistureRaw / 10.0f
        val temperature = temperatureRaw / 10.0f
        val humidity = humidityRaw / 10.0f
        val light = lightRaw.toFloat()

        // 构建 SensorData 对象
        val sensorData = SensorData(
            soilMoisture = soilMoisture,
            temperature = temperature,
            humidity = humidity,
            lightIntensity = light.toInt()
        )

        // 将 SensorData 转换为 JSON 字符串
        val sensorDataJson = gson.toJson(sensorData)

        // 更新设备信息
        viewModelScope.launch {
            try {
                // 获取当前设备信息
                val currentDevice = _device.value
                if (currentDevice != null) {
                    repository.updateSensorData(
                        address = deviceAddress,
                        sensorData = sensorDataJson,
                        lastConnectedTime = System.currentTimeMillis()
                    )
                }
            } catch (e: Exception) {
                Log.e("DeviceDetail", "更新传感器数据失败", e)
            }
        }

        // 构建 SensorDataVO 列表（与 UI 展示对应）
        val list = listOf(
            SensorDataVO(
                title = "环境温度",
                value = temperature.toInt().toString(),
                unit = "℃",
                status = if ((status and 0x02) != 0) SensorDataUtil.getTemperatureStatus(temperature).label else SensorStatus.INVALID.label,
                statusColor = if ((status and 0x02) != 0) SensorDataUtil.getTemperatureStatus(
                    temperature
                ).color else SensorStatus.INVALID.color,
                progress = SensorDataUtil.mapTemperatureToProgress(temperature),
                icon = R.drawable.ic_thermometer,
                iconColor = listOf(LightGreen, YellowGreen)
            ),
            SensorDataVO(
                title = "空气湿度",
                value = humidity.toInt().toString(),
                unit = "%",
                status = if ((status and 0x04) != 0) SensorDataUtil.getHumidityStatus(humidity).label else SensorStatus.INVALID.label,
                statusColor = if ((status and 0x04) != 0) SensorDataUtil.getHumidityStatus(humidity).color else SensorStatus.INVALID.color,
                progress = SensorDataUtil.mapHumidityToProgress(humidity),
                icon = R.drawable.ic_water,
                iconColor = listOf(SkyBlue, DarkBlue)
            ),
            SensorDataVO(
                title = "光照强度",
                value = light.toInt().toString(),
                unit = "Lux",
                status = if ((status and 0x08) != 0) SensorDataUtil.getLightStatus(light).label else SensorStatus.INVALID.label,
                statusColor = if ((status and 0x08) != 0) SensorDataUtil.getLightStatus(light).color else SensorStatus.INVALID.color,
                progress = SensorDataUtil.mapLightToProgress(light),
                icon = R.drawable.ic_sunny,
                iconColor = listOf(Yellow, OrangeYellow)
            ),
            SensorDataVO(
                title = "土壤湿度",
                value = soilMoisture.toInt().toString(),
                unit = "%",
                status = if ((status and 0x01) != 0) SensorDataUtil.getSoilMoistureStatus(
                    soilMoisture
                ).label else SensorStatus.INVALID.label,
                statusColor = if ((status and 0x01) != 0) SensorDataUtil.getSoilMoistureStatus(
                    soilMoisture
                ).color else SensorStatus.INVALID.color,
                progress = SensorDataUtil.mapSoilMoistureToProgress(soilMoisture),
                icon = R.drawable.ic_moisture,
                iconColor = listOf(OrangeYellow, OrangeRed)
            )
        )
        _sensorDataList.value = list
    }

    /**
     * 解析执行器状态（3 字节）
     * 字节0：水泵，字节1：风扇，字节2：补光灯
     */
    private fun parseActuatorState(data: ByteArray) {
        if (data.size < 3) return
        _pumpState.value = data[0].toInt() and 0xFF
        _fanState.value = data[1].toInt() and 0xFF
        _lightState.value = data[2].toInt() and 0xFF
    }

    /**
     * 发送获取传感器数据指令（AA 01 00 01 55）
     */
    @SuppressLint("MissingPermission")
    fun fetchSensorData() {
        val gatt = this.gatt
        val char = writeCharacteristic
        if (gatt == null || char == null) {
            _connectionState.value = ConnectionState.Failed("设备未连接")
            return
        }

        // 检查发送间隔
        val now = System.currentTimeMillis()
        if (now - lastGetSensorTime < MIN_REFRESH_INTERVAL) {
            Log.d("DeviceDetail", "请求过于频繁，忽略")
            return
        }
        lastGetSensorTime = now

        // 构造命令包
        val command = byteArrayOf(0xAA.toByte(), 0x01, 0x00, 0x01, 0x55.toByte())
        sendCommand(command)
    }

    /**
     * 发送获取执行器状态指令（AA 02 00 02 55）
     */
    @SuppressLint("MissingPermission")
    fun fetchActuatorState() {
        val gatt = this.gatt
        val char = writeCharacteristic
        if (gatt == null || char == null) {
            _connectionState.value = ConnectionState.Failed("设备未连接")
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastGetActuatorTime < MIN_REFRESH_INTERVAL) {
            Log.d("DeviceDetail", "请求过于频繁，忽略")
            return
        }
        lastGetActuatorTime = now

        val command = byteArrayOf(0xAA.toByte(), 0x02, 0x00, 0x02, 0x55.toByte())
        sendCommand(command)
    }

    /**
     * 设置执行器状态
     * @param actuatorId 执行器 ID (0:水泵,1:风扇,2:补光灯)
     * @param state 目标状态 (0:OFF,1:ON,2:ERROR)
     */
    @SuppressLint("MissingPermission")
    fun setActuator(actuatorId: Int, state: Int) {
        val gatt = this.gatt
        val char = writeCharacteristic
        if (gatt == null || char == null) {
            _connectionState.value = ConnectionState.Failed("设备未连接")
            return
        }

        // 构造命令包：AA 03 02 [ID] [State] 校验和 55
        val data = byteArrayOf(actuatorId.toByte(), state.toByte())
        val checksum = (0x03 xor 0x02 xor actuatorId xor state).toByte()
        val command = byteArrayOf(
            0xAA.toByte(),
            0x03,
            0x02,
            data[0],
            data[1],
            checksum,
            0x55.toByte()
        )
        sendCommand(command)

        // 乐观更新 UI（可选，也可等待设备响应后重新获取）
        // 这里选择等待设备返回 ACK，在 handleReceivedData 中会重新获取执行器状态
    }

    /**
     * 发送原始命令字节
     */
    @SuppressLint("MissingPermission")
    private fun sendCommand(command: ByteArray) {
        val gatt = this.gatt
        val char = writeCharacteristic
        if (gatt == null || char == null) return

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
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
            _connectionState.value = ConnectionState.Failed("发送异常")
        }
    }

    /**
     * 启动定时轮询（传感器和执行器状态交替获取）
     */
    private fun startPolling() {
        if (isPollingActive) return
        isPollingActive = true
        viewModelScope.launch {
            while (isPollingActive) {
                fetchSensorData()
                delay(1000) // 间隔1秒再获取执行器状态
                fetchActuatorState()
                delay(AUTO_REFRESH_INTERVAL - 1000) // 保持总周期约为 AUTO_REFRESH_INTERVAL
            }
        }
    }

    private fun stopPolling() {
        isPollingActive = false
    }

    /**
     * 手动刷新（由下拉刷新触发）
     */
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

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}