package com.codeintellix.envlink.entity.sensor

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
data class SensorData(
    val soilMoisture: Float, // %
    val temperature: Float, // ℃
    val humidity: Float, // %
    val lightIntensity: Int, // Lux
    val statusFlags: Int? = null, // 位标志
    val timestamp: Long? = null // 秒
) {
    fun isSoilValid() = (statusFlags?.and(0x01)) != 0
    fun isTemperatureValid() = (statusFlags?.and(0x02)) != 0
    fun isHumidityValid() = (statusFlags?.and(0x04)) != 0
    fun isLightValid() = (statusFlags?.and(0x08)) != 0
}