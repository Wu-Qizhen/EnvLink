package com.codeintellix.envlink.entity.protocol

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
data class ControlParams(
    val soilMoistureLow: Float,
    val soilMoistureHigh: Float,
    val temperatureHigh: Float,
    val temperatureLow: Float,
    val lightIntensityLow: Float,
    val lightIntensityHigh: Float,
    val minPumpInterval: Long, // 秒
    val maxPumpDuration: Long // 秒
) {
    companion object {
        val DEFAULT = ControlParams(
            soilMoistureLow = 40.0f,
            soilMoistureHigh = 60.0f,
            temperatureHigh = 30.0f,
            temperatureLow = 28.0f,
            lightIntensityLow = 500.0f,
            lightIntensityHigh = 800.0f,
            minPumpInterval = 300,
            maxPumpDuration = 30
        )
    }
}