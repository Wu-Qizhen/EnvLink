package com.codeintellix.envlink.entity.device

import java.util.UUID

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
object BleUuid {
    // BLE 相关常量（与 BT24 模块匹配）
    val SERVICE_UUID: UUID? = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
    val TX_CHAR_UUID: UUID? = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    val RX_CHAR_UUID: UUID? = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    val CCCD_UUID: UUID? = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
}