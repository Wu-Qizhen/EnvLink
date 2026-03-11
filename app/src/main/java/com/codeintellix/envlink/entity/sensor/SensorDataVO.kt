package com.codeintellix.envlink.entity.sensor

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.03
 */
data class SensorDataVO(
    var title: String, // 传感器名称
    var value: String, // 数值”
    var unit: String, // 单位
    var status: SensorStatus, // 状态
    var progress: Float, // 进度条百分比
    @DrawableRes var icon: Int, // 图标
    var iconColor: List<Color> // 图标背景渐变
)