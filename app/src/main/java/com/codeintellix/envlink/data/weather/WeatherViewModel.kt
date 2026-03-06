package com.codeintellix.envlink.data.weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeintellix.envlink.entity.weather.Weather
import com.codeintellix.envlink.entity.weather.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.06
 */
class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val client = OkHttpClient()

    /**
     * 公开的刷新方法，由 UI 调用（例如点击天气区域）
     */
    fun refreshWeather(context: Context) {
        _weatherState.value = WeatherState.Loading
        fetchWeather(context)
    }

    /**
     * 获取位置并请求天气
     */
    fun fetchWeather(context: Context) {
        viewModelScope.launch {
            // 1. 获取位置（优先使用最后已知位置，若无效则尝试单次定位）
            val location = getLocation(context)
            if (location == null) {
                _weatherState.value = WeatherState.Error("无法获取位置，请检查权限或开启位置服务")
                return@launch
            }

            // 2. 请求天气
            val weatherResult = requestWeather(location.latitude, location.longitude)
            if (weatherResult == null) {
                _weatherState.value = WeatherState.Error("天气获取失败，请稍后重试")
            } else {
                _weatherState.value = WeatherState.Success(
                    weather = weatherResult.first,
                    temperature = weatherResult.second
                )
            }
        }
    }

    /**
     * 综合定位方法：先尝试获取缓存位置，若无效则主动请求一次实时位置
     */
    private suspend fun getLocation(context: Context): Location? = withContext(Dispatchers.IO) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 1. 尝试获取最后已知位置（网络定位）
        var location: Location? = null
        try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        } catch (_: SecurityException) {
            // 权限不足，应由调用方处理，此处返回 null 即可
        }

        // 2. 如果缓存位置无效（null 或太旧），尝试请求一次实时定位
        if (location == null || isLocationTooOld(location)) {
            location = requestSingleLocation(context)
        }

        location
    }

    /**
     * 判断位置是否太旧（超过 30 分钟视为无效，可根据需要调整）
     */
    private fun isLocationTooOld(location: Location): Boolean {
        val now = System.currentTimeMillis()
        return (now - location.time) > 30 * 60 * 1000 // 30 分钟
    }

    /**
     * 请求一次单次定位（使用网络提供者，兼容 API 21+）
     * 此方法为挂起函数，会等待定位结果或超时（默认 10 秒）
     */
    @SuppressLint("MissingPermission")
    private suspend fun requestSingleLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 检查网络提供者是否可用
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    // 一旦收到位置，立即恢复并返回
                    continuation.resume(location)
                    locationManager.removeUpdates(this)
                }
            }

            // 设置超时（10 秒），避免无限等待
            val timeoutJob = viewModelScope.launch {
                kotlinx.coroutines.delay(10000) // 10 秒超时
                if (continuation.isActive) {
                    continuation.resume(null)
                    locationManager.removeUpdates(listener)
                }
            }

            try {
                // 请求位置更新（使用网络提供者，低功耗）
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L, // 最小时间间隔
                    0f, // 最小距离间隔
                    listener,
                    Looper.getMainLooper()
                )
            } catch (_: Exception) {
                // 例如权限错误、提供者不支持等
                continuation.resume(null)
                timeoutJob.cancel()
            }

            // 协程取消时清理
            continuation.invokeOnCancellation {
                locationManager.removeUpdates(listener)
                timeoutJob.cancel()
            }
        }
    }

    /**
     * 请求 Open-Meteo API，返回 (天气枚举, 温度字符串)
     */
    private suspend fun requestWeather(lat: Double, lon: Double): Pair<Weather, String>? =
        withContext(Dispatchers.IO) {
            val url =
                "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon¤t=temperature_2m,weather_code&timezone=auto"
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@withContext null

                val json = JSONObject(response.body?.string() ?: return@withContext null)
                val current = json.getJSONObject("current")
                val temp = current.getDouble("temperature_2m")
                val code = current.getInt("weather_code")

                val weatherEnum = weatherCodeToEnum(code)
                val tempStr = "${temp.toInt()}°" // 保留整数值，如 28°

                Pair(weatherEnum, tempStr)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

    /**
     * 将 WMO 天气代码映射到您定义的 Weather 枚举
     * 参考：https://open-meteo.com/en/docs
     */
    private fun weatherCodeToEnum(code: Int): Weather {
        return when (code) {
            0 -> Weather.SUNNY
            1, 2, 3 -> Weather.CLOUDY // 晴间多云、多云等
            45, 48 -> Weather.FOG
            51, 53, 55 -> Weather.HAZE // 毛毛雨也可用
            56, 57 -> Weather.SLEET // 冻毛毛雨
            61, 63, 65 -> Weather.SMALL_RAIN // 小雨、中雨等
            66, 67 -> Weather.SLEET // 冻雨
            71, 73, 75 -> Weather.LIGHT_SNOW
            77 -> Weather.SNOW // 雪粒
            80, 81, 82 -> Weather.SHOWER
            85, 86 -> Weather.MODERATE_SNOW
            95 -> Weather.THUNDERSHOWER
            96, 99 -> Weather.RAINSTORM // 冰雹雷暴近似为
            else -> Weather.CLOUDY // 默认
        }
    }
}