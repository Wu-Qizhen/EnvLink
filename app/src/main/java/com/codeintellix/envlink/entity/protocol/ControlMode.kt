package com.codeintellix.envlink.entity.protocol

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
enum class ControlMode(val value: Int) {
    AUTO(0),
    MANUAL(1),
    CALIBRATION(2);

    companion object {
        fun fromInt(value: Int): ControlMode? = ControlMode.entries.find { it.value == value }
    }
}