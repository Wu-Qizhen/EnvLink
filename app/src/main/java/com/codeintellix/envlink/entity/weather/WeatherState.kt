package com.codeintellix.envlink.entity.weather

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.06
 */
sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: Weather, val temperature: String) : WeatherState()
    data class Error(val message: String) : WeatherState()
}