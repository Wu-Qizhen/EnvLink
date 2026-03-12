package com.codeintellix.envlink.data.device

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeintellix.envlink.entity.device.Device
import kotlinx.coroutines.flow.Flow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device)

    @Update
    suspend fun updateDevice(device: Device)

    @Query("UPDATE devices SET lastConnectedTime = :lastConnectedTime, latestSensorData = :sensorData WHERE address = :address")
    suspend fun updateSensorData(address: String, lastConnectedTime: Long, sensorData: String)

    @Query("UPDATE devices SET firmwareVersion = :firmwareVersion WHERE address = :address")
    suspend fun updateFirmwareVersion(address: String, firmwareVersion: String)

    @Query("SELECT * FROM devices ORDER BY lastConnectedTime DESC")
    fun getAllDevices(): Flow<List<Device>>

    @Query("DELETE FROM devices WHERE address = :address")
    suspend fun deleteDevice(address: String)
}