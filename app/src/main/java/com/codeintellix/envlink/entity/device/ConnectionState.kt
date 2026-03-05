package com.codeintellix.envlink.entity.device

import android.bluetooth.BluetoothDevice

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
sealed class ConnectionState {
    object Idle : ConnectionState()
    object Connecting : ConnectionState()
    data class Connected(val device: BluetoothDevice) : ConnectionState()
    data class Failed(val error: String) : ConnectionState()
    object Disconnected : ConnectionState()
}