package com.codeintellix.envlink.entity.actuator

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
enum class ActuatorType(val value: Int) {
    PUMP(0),
    FAN(1),
    LIGHT(2);

    companion object {
        fun fromInt(value: Int): ActuatorType? = ActuatorType.entries.find { it.value == value }
    }
}