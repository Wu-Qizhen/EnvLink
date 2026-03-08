package com.codeintellix.envlink.entity.device

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val room: String = "阳台",
    // val addTime: Long = System.currentTimeMillis(),
    val lastConnected: Long = System.currentTimeMillis()
)