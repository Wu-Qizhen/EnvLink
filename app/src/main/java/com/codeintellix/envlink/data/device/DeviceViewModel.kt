package com.codeintellix.envlink.data.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeintellix.envlink.domain.device.BluetoothScanner
import com.codeintellix.envlink.entity.device.ConnectionState
import com.codeintellix.envlink.entity.device.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.reflect.Method
import java.util.UUID

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class DeviceViewModel(
    private val repository: DeviceRepository,
    private val bluetoothScanner: BluetoothScanner,
    context: Context
) : ViewModel() {
    private val appContext: Context = context.applicationContext
    private var scanJob: Job? = null // 扫描协程的 Job
    private var gatt: BluetoothGatt? = null // BLE Gatt 客户端

    private val scanTimeout = 10_000L // 毫秒
    private val deviceName = "PlantGuard"

    // 预设配对码（必须与 STM32 端 AT+PIN 设置的一致）
    private val presetPin = "McEnvCtr"

    // BLE 服务与特征 UUID（与 BT24 模块匹配）
    private val serviceUuid = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
    private val txCharUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    private val rxCharUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")

    // 保存找到的写特征和通知特征，供后续通信使用
    private var writeCharacteristic: BluetoothGattCharacteristic? = null
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null

    // 状态 Flow
    private val _scanResults = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scanResults: StateFlow<List<BluetoothDevice>> = _scanResults

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _receivedData = MutableStateFlow("")
    val receivedData: StateFlow<String> = _receivedData

    private var _currentDevice: BluetoothDevice? = null
    val currentDevice: BluetoothDevice? get() = _currentDevice

    private val _addedDevices = MutableStateFlow<List<Device>>(emptyList())
    val addedDevices: StateFlow<List<Device>> = _addedDevices

    private val discoveredDevices = mutableSetOf<BluetoothDevice>()
    private var pendingDeviceAddress: String? = null

    // 广播接收器：处理配对请求和绑定状态变化
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                    // 收到配对请求，自动设置 PIN
                    val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    // 仅处理我们正在连接的设备
                    if (device?.address == pendingDeviceAddress) {
                        device?.let { handlePairingRequest(it) }
                    }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    /*val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }*/
                    val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    when (bondState) {
                        BluetoothDevice.BOND_BONDED -> {
                            Toast.makeText(appContext, "配对成功", Toast.LENGTH_SHORT).show()
                            // 配对成功后不需要再次连接，GATT 连接会继续
                            // 配对成功后，重新启用通知（确保通知有效）
                            reactivateNotification()
                        }

                        BluetoothDevice.BOND_NONE -> {
                            Toast.makeText(appContext, "配对失败", Toast.LENGTH_SHORT).show()
                            // 可清理状态
                        }
                    }
                }
            }
        }
    }

    // GATT 回调
    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    gatt.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    // 更新状态，并清理资源
                    _connectionState.value = ConnectionState.Disconnected
                    gatt.close()
                    this@DeviceViewModel.gatt = null
                    writeCharacteristic = null
                    notifyCharacteristic = null
                    pendingDeviceAddress = null
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
        ) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                @Suppress("DEPRECATION") val value = characteristic.value
                if (value != null) {
                    val received = String(value, Charsets.UTF_8)
                    _receivedData.value = _receivedData.value + received
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            // Android 13+ 会调用此方法
            val received = String(value, Charsets.UTF_8)
            _receivedData.value = _receivedData.value + received
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

    init {
        // 注册广播：配对请求 + 绑定状态变化
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        appContext.registerReceiver(bluetoothReceiver, filter)

        // 收集已添加设备列表
        viewModelScope.launch {
            repository.getAllDevices().collect { devices ->
                _addedDevices.value = devices
            }
        }
    }

    override fun onCleared() {
        appContext.unregisterReceiver(bluetoothReceiver)
        disconnectDevice() // 断开并释放 GATT 资源
        super.onCleared()
    }

    // 处理配对请求：通过反射设置 PIN 并确认
    @SuppressLint("MissingPermission")
    private fun handlePairingRequest(device: BluetoothDevice) {
        try {
            // 反射调用 setPin 方法
            val setPinMethod: Method = device.javaClass.getMethod("setPin", ByteArray::class.java)
            setPinMethod.invoke(device, presetPin.toByteArray(Charsets.UTF_8))

            // 确认配对（某些设备可能需要）
            val setPairingConfirmationMethod: Method? = try {
                device.javaClass.getMethod("setPairingConfirmation", Boolean::class.java)
            } catch (_: NoSuchMethodException) {
                null
            }
            setPairingConfirmationMethod?.invoke(device, true)
        } catch (e: Exception) {
            e.printStackTrace()
            // 反射失败，降级为系统默认处理（会弹出配对对话框）
        }
    }

    @SuppressLint("MissingPermission")
    private fun reactivateNotification() {
        val gatt = this.gatt
        val notifyChar = notifyCharacteristic
        if (gatt != null && notifyChar != null) {
            // 重新启用通知
            gatt.setCharacteristicNotification(notifyChar, true)
            val descriptor = notifyChar.getDescriptor(cccdUuid)
            descriptor?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gatt.writeDescriptor(it, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                } else {
                    @Suppress("DEPRECATION")
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    @Suppress("DEPRECATION")
                    gatt.writeDescriptor(it)
                }
            }
        }
    }

    // BLE 扫描
    fun startBleScan() {
        _currentDevice = null
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _isScanning.value = true
            discoveredDevices.clear()
            try {
                // 设置扫描超时
                withTimeout(scanTimeout) {
                    bluetoothScanner.startBleScan(deviceName)
                        .catch { e -> /* 处理错误 */ }
                        .collect { device ->
                            discoveredDevices.add(device)
                            _scanResults.value = discoveredDevices.toList()
                        }
                }
            } catch (_: kotlinx.coroutines.TimeoutCancellationException) {
                // 超时处理：可更新一个状态提示 UI，或直接静默结束（_isScanning 已在 finally 中置 false）
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
    }

    // 添加设备（保存到数据库并连接）
    fun addDevice(device: BluetoothDevice) {
        stopScan()
        viewModelScope.launch {
            _currentDevice = device
            // 保存到数据库
            val entity = try {
                Device(
                    address = device.address,
                    name = device.name ?: "未知设备"
                )
            } catch (_: SecurityException) {
                Device(
                    address = device.address,
                    name = "未知设备"
                )
            }
            repository.addDevice(entity)

            // 记录待连接设备地址，用于配对时过滤
            pendingDeviceAddress = device.address
            connectToDevice(device)
        }
    }

    // 连接设备（BLE GATT）
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        // 断开现有连接
        disconnectDevice()
        viewModelScope.launch(Dispatchers.Main) {
            _connectionState.value = ConnectionState.Connecting
            gatt = device.connectGatt(appContext, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        }
    }

    // 断开连接
    @SuppressLint("MissingPermission")
    fun disconnectDevice() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
        writeCharacteristic = null
        notifyCharacteristic = null
        _connectionState.value = ConnectionState.Disconnected
        pendingDeviceAddress = null
    }

    // 发送数据
    @SuppressLint("MissingPermission")
    fun sendData(data: String) {
        val gatt = this.gatt
        val char = writeCharacteristic
        if (gatt != null && char != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gatt.writeCharacteristic(
                        char,
                        data.toByteArray(Charsets.UTF_8),
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    )
                } else {
                    @Suppress("DEPRECATION")
                    char.value = data.toByteArray(Charsets.UTF_8)
                    @Suppress("DEPRECATION")
                    gatt.writeCharacteristic(char)
                }
            } catch (_: Exception) {
                _connectionState.value = ConnectionState.Failed("发送异常")
            }
        } else {
            _connectionState.value = ConnectionState.Failed("设备未连接")
        }
    }

    fun retryConnection() {
        _currentDevice?.let { connectToDevice(it) }
    }

    fun updateDeviceName(address: String, newName: String) {
        viewModelScope.launch {
            repository.updateDevice(Device(address, newName))
        }
    }
}

/*class DeviceViewModel(
    private val repository: DeviceRepository,
    private val bluetoothScanner: BluetoothScanner,
    context: Context
) : ViewModel() {
    private val appContext: Context = context.applicationContext
    private var scanJob: Job? = null // 扫描协程的 Job
    private var listenerJob: Job? = null

    // 预先定义的配对码（应与 STM32 端 HC-05 设置的 PIN 码一致）
    private val presetPin = "McEnvCtr"
    private val sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val _scanResults = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scanResults: StateFlow<List<BluetoothDevice>> = _scanResults

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _receivedData = MutableStateFlow("")
    val receivedData: StateFlow<String> = _receivedData

    private val discoveredDevices = mutableSetOf<BluetoothDevice>()
    private var bluetoothSocket: BluetoothSocket? = null

    // 广播接收器监听配对状态变化
    private val bondStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                when (bondState) {
                    BluetoothDevice.BOND_BONDED -> {
                        // 配对成功，连接设备
                        device?.let { connectToDevice(it) }
                        Toast.makeText(appContext, "配对成功", Toast.LENGTH_SHORT).show()
                    }

                    BluetoothDevice.BOND_NONE -> {
                        // 配对失败，可提示用户
                        Toast.makeText(appContext, "配对失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    init {
        // 注册广播接收器
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        appContext.registerReceiver(bondStateReceiver, filter)
    }

    override fun onCleared() {
        appContext.unregisterReceiver(bondStateReceiver)
        disconnectDevice()
        super.onCleared()
    }

    fun startScan() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _isScanning.value = true
            discoveredDevices.clear()
            try {
                bluetoothScanner.startScan()
                    .catch { e ->
                        // 处理错误
                    }
                    .collect { device ->
                        discoveredDevices.add(device)
                        _scanResults.value = discoveredDevices.toList()
                    }
            } finally {
                // 无论正常结束还是取消，都将扫描状态置为 false
                _isScanning.value = false
            }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
    }

    fun addDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            // 保存到数据库
            val entity = try {
                Device(
                    address = device.address,
                    name = device.name ?: "未知设备"
                )
            } catch (_: SecurityException) {
                Device(
                    address = device.address,
                    name = "未知设备"
                )
            }
            repository.addDevice(entity)

            // 开始配对或连接
            pairOrConnect(device)
        }
    }

    private fun pairOrConnect(device: BluetoothDevice) {
        try {
            when (device.bondState) {
                BluetoothDevice.BOND_BONDED -> {
                    // 已配对，直接连接
                    connectToDevice(device)
                }

                else -> {
                    // 未配对，使用预设 PIN 码配对
                    pairWithPin(device)
                }
            }
        } catch (_: SecurityException) {
            // 权限不足，可提示用户
            Toast.makeText(appContext, "权限不足，请授予权限", Toast.LENGTH_SHORT).show()
        }
    }

    // 通过反射设置 PIN 码并发起配对
    @SuppressLint("MissingPermission")
    private fun pairWithPin(device: BluetoothDevice) {
        try {
            // 反射调用 setPin 方法
            val setPinMethod: Method = device.javaClass.getMethod("setPin", ByteArray::class.java)
            setPinMethod.invoke(device, presetPin.toByteArray(Charsets.UTF_8))

            // 调用 createBond 发起配对
            device.createBond()
        } catch (e: Exception) {
            e.printStackTrace()
            // 反射失败则降级为系统默认配对（用户需手动输入 PIN）
            device.createBond()
        }
    }

    // 建立蓝牙 Socket 连接
    fun connectToDevice(device: BluetoothDevice) {
        // 取消当前任何连接
        disconnectDevice()

        viewModelScope.launch(Dispatchers.IO) {
            _connectionState.value = ConnectionState.Connecting
            bluetoothScanner.cancelDiscovery()

            val maxRetries = 3
            var socket: BluetoothSocket? = null
            var success = false

            for (retryCount in 0 until maxRetries) {
                try {
                    socket = device.createRfcommSocketToServiceRecord(sppUuid)
                    withTimeout(10_000L) {
                        socket!!.connect()
                    }
                    success = true
                    break
                } catch (_: Exception) {
                    socket?.close()
                    socket = null
                    if (retryCount == maxRetries - 1) {
                        // 最后一次重试失败，不继续循环
                        _connectionState.value = ConnectionState.Failed("连接失败，请稍后重试")
                        return@launch
                    }
                    delay(1000L * (retryCount + 1)) // 退避时间
                }
            }

            if (success) {
                // 连接成功，保存 socket 并启动监听
                bluetoothSocket = socket
                _connectionState.value = ConnectionState.Connected(device)
                startListening(socket!!)
                Toast.makeText(appContext, "连接成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startListening(socket: BluetoothSocket) {
        listenerJob = viewModelScope.launch(Dispatchers.IO) {
            val inputStream = socket.inputStream
            val buffer = ByteArray(1024)
            try {
                while (isActive) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        val received = String(buffer, 0, bytesRead, Charsets.UTF_8)
                        _receivedData.value = _receivedData.value + received
                    }
                }
            } catch (_: IOException) {
                // 读取异常
            } finally {
                disconnectDevice()
            }
        }
    }

    fun sendData(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val socket = bluetoothSocket
            if (socket != null && socket.isConnected) {
                try {
                    socket.outputStream.write(data.toByteArray(Charsets.UTF_8))
                    socket.outputStream.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                    // 发送失败，可更新状态为失败
                    _connectionState.value = ConnectionState.Failed("发送失败")
                }
            } else {
                // 未连接时提示
                _connectionState.value = ConnectionState.Failed("设备未连接")
            }
        }
    }

    fun disconnectDevice() {
        listenerJob?.cancel()
        listenerJob = null
        closeSocket()
        _connectionState.value = ConnectionState.Disconnected
    }

    private fun closeSocket() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bluetoothSocket = null
        }
    }
}*/