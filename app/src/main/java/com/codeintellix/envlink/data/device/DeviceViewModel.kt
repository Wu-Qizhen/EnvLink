package com.codeintellix.envlink.data.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeintellix.envlink.domain.device.BluetoothScanner
import com.codeintellix.envlink.entity.device.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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
    private val _scanResults = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scanResults: StateFlow<List<BluetoothDevice>> = _scanResults

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private var scanJob: Job? = null // 扫描协程的 Job

    private val discoveredDevices = mutableSetOf<BluetoothDevice>()

    // 预先定义的配对码（应与 STM32 端 HC-05 设置的 PIN 码一致）
    private val presetPin = "SPG"
    private val sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val appContext: Context = context.applicationContext

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
                    }

                    BluetoothDevice.BOND_NONE -> {
                        // 配对失败，可提示用户
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
    private fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch(Dispatchers.IO) {
            var socket: BluetoothSocket?
            try {
                // 先取消扫描，避免影响连接
                bluetoothScanner.cancelDiscovery()

                socket = device.createRfcommSocketToServiceRecord(sppUuid)
                socket.connect()

                // 连接成功，保存 socket 以备后续通信
            } catch (e: IOException) {
                e.printStackTrace()
                // 连接失败，更新 UI
            }
        }
    }
}