package com.codeintellix.envlink.domain.sensor

import com.codeintellix.envlink.entity.sensor.SensorStatus

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
object SensorDataHelper {
    // 进度条映射（限制在 0~1）
    fun mapTemperatureToProgress(temp: Float): Float =
        ((temp + 50) / 200).coerceIn(0f, 1f)

    fun mapHumidityToProgress(hum: Float): Float =
        (hum / 100).coerceIn(0f, 1f)

    fun mapLightToProgress(light: Float): Float =
        (light / 50000f).coerceIn(0f, 1f)

    fun mapSoilMoistureToProgress(moisture: Float): Float =
        (moisture / 100).coerceIn(0f, 1f)

    // 状态判断（阈值可根据需要调整）
    fun getTemperatureStatus(temp: Float): SensorStatus = when (temp) {
        in 15.0f..30.0f -> SensorStatus.NORMAL
        in 10.0f..15.0f, in 30.0f..35.0f -> SensorStatus.WARNING
        else -> SensorStatus.DANGER
    }

    fun getHumidityStatus(hum: Float): SensorStatus = when (hum) {
        in 40.0f..70.0f -> SensorStatus.NORMAL
        in 30.0f..40.0f, in 70.0f..80.0f -> SensorStatus.WARNING
        else -> SensorStatus.DANGER
    }

    fun getLightStatus(light: Float): SensorStatus = when (light) {
        in 1000f..20000f -> SensorStatus.NORMAL
        in 500f..999f, in 20001f..30000f -> SensorStatus.WARNING
        else -> SensorStatus.DANGER
    }

    fun getSoilMoistureStatus(moisture: Float): SensorStatus = when (moisture) {
        in 20.0f..80.0f -> SensorStatus.NORMAL
        in 10.0f..20.0f, in 80.0f..90.0f -> SensorStatus.WARNING
        else -> SensorStatus.DANGER
    }
}