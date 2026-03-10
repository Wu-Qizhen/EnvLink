package com.codeintellix.envlink.entity.actuator

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
enum class ActuatorState(val value: Int) {
    OFF(0),
    ON(1),
    ERROR(2);

    companion object {
        fun fromInt(value: Int): ActuatorState? = ActuatorState.entries.find { it.value == value }
    }
}