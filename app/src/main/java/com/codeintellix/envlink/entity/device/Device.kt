package com.codeintellix.envlink.entity.device

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeintellix.envlink.entity.house.RoomType

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val address: String, // MAC 地址作为主键
    val name: String,
    val room: String = RoomType.BALCONY.displayName,
    val createTime: Long = System.currentTimeMillis(), // 添加时间
    val lastConnectedTime: Long = System.currentTimeMillis(),
    val latestSensorData: String = "", // 最新传感器数据，以 JSON 字符串格式存储
    val firmwareVersion: String = "" // 固件版本（系统版本）
)