package com.codeintellix.envlink.data.device

import android.content.Context
import com.codeintellix.envlink.data.app.AppDatabase
import com.codeintellix.envlink.entity.device.Device
import kotlinx.coroutines.flow.Flow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class DeviceRepository(private val deviceDao: DeviceDao) {
    fun getAllDevices(): Flow<List<Device>> = deviceDao.getAllDevices()

    suspend fun addDevice(device: Device) = deviceDao.insertDevice(device)

    suspend fun updateDevice(device: Device) = deviceDao.updateDevice(device)

    suspend fun updateSensorData(address: String, lastConnectedTime: Long, sensorData: String) {
        deviceDao.updateSensorData(address, lastConnectedTime, sensorData)
    }

    suspend fun removeDevice(address: String) = deviceDao.deleteDevice(address)

    companion object {
        @Volatile
        private var INSTANCE: DeviceRepository? = null

        fun getInstance(context: Context): DeviceRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getInstance(context)
                val instance = DeviceRepository(database.deviceDao())
                INSTANCE = instance
                instance
            }
        }
    }
}