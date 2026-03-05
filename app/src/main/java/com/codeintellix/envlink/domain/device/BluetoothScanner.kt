package com.codeintellix.envlink.domain.device

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class BluetoothScanner(private val context: Context) {
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
}