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
    private var scanJob: Job? = null          // 扫描协程的 Job
    private var gatt: BluetoothGatt? = null   // BLE Gatt 客户端

    // BLE 服务与特征 UUID
    // TODO
    private val serviceUuid = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
    private val txCharUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB") // 手机 -> 模块（写）
    private val rxCharUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB") // 模块 -> 手机（通知）
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB") // 不变

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

    private val discoveredDevices = mutableSetOf<BluetoothDevice>()

    // 广播接收器，用于监听配对状态变化
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
                        // 配对成功，可自动连接或提示
                        device?.let { connectToDevice(it) }
                        Toast.makeText(appContext, "配对成功", Toast.LENGTH_SHORT).show()
                    }

                    BluetoothDevice.BOND_NONE -> {
                        Toast.makeText(appContext, "配对失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    init {
        // 注册配对状态广播（非必需，但保留有助于调试）
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        appContext.registerReceiver(bondStateReceiver, filter)
    }

    override fun onCleared() {
        appContext.unregisterReceiver(bondStateReceiver)
        disconnectDevice() // 断开并释放 GATT 资源
        super.onCleared()
    }

    // BLE 扫描
    fun startBleScan() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _isScanning.value = true
            discoveredDevices.clear()
            try {
                bluetoothScanner.startBleScan()
                    .catch { e -> /* 可处理错误，如权限不足等 */ }
                    .collect { device ->
                        discoveredDevices.add(device)
                        _scanResults.value = discoveredDevices.toList()
                    }
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        // 可调用 bluetoothScanner.stopBleScan()
    }

    // 添加设备（保存到数据库并连接）
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

            // 直接连接（系统会自动处理配对弹窗）
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

            // 停止经典蓝牙扫描（如果有），避免影响连接
            // bluetoothScanner.cancelDiscovery()

            // 建立 GATT 连接
            gatt =
                device.connectGatt(appContext, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        }
    }

    // GATT 回调
    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    // 连接成功，开始发现服务
                    gatt.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    // 更新状态，并清理资源
                    _connectionState.value = ConnectionState.Disconnected
                    gatt.close()
                    this@DeviceViewModel.gatt = null
                    writeCharacteristic = null
                    notifyCharacteristic = null
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(serviceUuid)
                if (service != null) {
                    // 获取写特征
                    writeCharacteristic = service.getCharacteristic(txCharUuid)
                    // 获取通知特征
                    val rxChar = service.getCharacteristic(rxCharUuid)
                    if (rxChar != null) {
                        // 启用通知
                        gatt.setCharacteristicNotification(rxChar, true)
                        // 配置 CCCD 描述符以接收通知
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
            characteristic: BluetoothGattCharacteristic
        ) {
            // 接收到设备发来的数据
            val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ 会通过重载方法提供 value 参数
                // 这个方法不会被调用
                null
            } else {
                @Suppress("DEPRECATION")
                characteristic.value
            }
            if (value != null) {
                val received = String(value, Charsets.UTF_8)
                _receivedData.value = _receivedData.value + received
            }
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

    // 断开连接
    @SuppressLint("MissingPermission")
    fun disconnectDevice() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
        writeCharacteristic = null
        notifyCharacteristic = null
        _connectionState.value = ConnectionState.Disconnected
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
    private val presetPin = "1234"
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