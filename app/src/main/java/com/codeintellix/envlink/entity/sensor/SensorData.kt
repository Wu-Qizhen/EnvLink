package com.codeintellix.envlink.entity.sensor

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
data class SensorData(
    val soilMoisture: Float,      // 土壤湿度（%）
    val temperature: Float,       // 温度（℃）
    val humidity: Float,          // 空气湿度（%）
    val lightIntensity: Int,      // 光照强度（Lux）
    val timestamp: Long,          // 时间戳（秒）
    val validSensors: Int         // 有效传感器标志位
)