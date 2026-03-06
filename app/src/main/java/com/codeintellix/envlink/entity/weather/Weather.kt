package com.codeintellix.envlink.entity.weather

import androidx.annotation.DrawableRes
import com.codeintellix.envlink.R

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.06
 */
enum class Weather(@DrawableRes val icon: Int) {
    BLIZZARD(R.drawable.weather_blizzard),
    CLOUDY(R.drawable.weather_cloudy),
    FOG(R.drawable.weather_fog),
    HAILSTONE(R.drawable.weather_hailstone),
    HAZE(R.drawable.weather_haze),
    HEAVY_RAIN(R.drawable.weather_heavy_rain),
    HEAVY_SNOW(R.drawable.weather_heavy_snow),
    LIGHT_SNOW(R.drawable.weather_light_snow),
    MODERATE_RAIN(R.drawable.weather_moderate_rain),
    MODERATE_SNOW(R.drawable.weather_moderate_snow),
    OVERCAST_SKY(R.drawable.weather_overcast_sky),
    RAINSTORM(R.drawable.weather_rainstorm),
    SAND_BLOWING(R.drawable.weather_sand_blowing),
    SHOWER(R.drawable.weather_shower),
    SLEET(R.drawable.weather_sleet),
    SMALL_RAIN(R.drawable.weather_small_rain),
    SNOW(R.drawable.weather_snow),
    SUNNY(R.drawable.weather_sunny),
    THUNDERSHOWER(R.drawable.weather_thundershower),
}