package com.codeintellix.envlink.data.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.IOException
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
                    .catch { e -> /* 处理错误 */ }
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
}