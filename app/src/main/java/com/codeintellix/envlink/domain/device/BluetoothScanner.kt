package com.codeintellix.envlink.domain.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class BluetoothScanner(context: Context) {
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

    fun startBleScan(deviceNameFilter: String? = null): Flow<BluetoothDevice> = callbackFlow {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            close()
            return@callbackFlow
        }

        val scanCallback = object : ScanCallback() {
            @SuppressLint("MissingPermission")
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let { device ->
                    // 根据设备名称过滤
                    val shouldSend = if (deviceNameFilter != null) {
                        device.name?.contains(deviceNameFilter, ignoreCase = true) == true
                    } else {
                        true
                    }

                    if (shouldSend) {
                        trySend(device)
                    }
                }
            }
        }

        // 扫描设置
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        try {
            bluetoothLeScanner?.startScan(null, settings, scanCallback)
        } catch (_: SecurityException) {
            close()
            return@callbackFlow
        }

        awaitClose {
            try {
                bluetoothLeScanner?.stopScan(scanCallback)
            } catch (_: SecurityException) {
            }
        }
    }

    /*fun stopBleScan() {
        // 停止扫描的方法会在 awaitClose 中自动调用，无需额外实现
    }*/
}

/*class BluetoothScanner(private val context: Context) {
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    fun startScan(): Flow<BluetoothDevice> = callbackFlow {
        // 检查蓝牙是否开启
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            close() // 或发送错误
            return@callbackFlow
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                intent.getParcelableExtra(
                                    BluetoothDevice.EXTRA_DEVICE,
                                    BluetoothDevice::class.java
                                )
                            } else {
                                @Suppress("DEPRECATION")
                                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            }
                        device?.let { trySend(it) }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        close() // 扫描结束，关闭流
                    }
                }
            }
        }

        // 注册接收器
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        context.registerReceiver(receiver, filter)

        // 开始扫描
        try {
            bluetoothAdapter.startDiscovery()
        } catch (_: SecurityException) {
            close()
            return@callbackFlow
        }

        // 取消扫描并注销接收器
        awaitClose {
            try {
                bluetoothAdapter.cancelDiscovery()
            } catch (_: SecurityException) {
                // 忽略取消时的权限异常
            }
            context.unregisterReceiver(receiver)
        }
    }

    fun cancelDiscovery() {
        try {
            bluetoothAdapter?.cancelDiscovery()
        } catch (_: SecurityException) {
            // 忽略
        }
    }
}*/