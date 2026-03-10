package com.codeintellix.envlink.entity.actuator

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
data class ActuatorStatus(
    val pump: ActuatorState,
    val fan: ActuatorState,
    val light: ActuatorState
)