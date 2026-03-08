package com.codeintellix.envlink.entity.sensor

import androidx.compose.ui.graphics.Color
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.OrangeYellow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
enum class SensorStatus(val label: String, val color: Color) {
    NORMAL("正常", LightGreen),
    WARNING("注意", OrangeYellow),
    DANGER("危险", OrangeRed),
    INVALID("失效", OrangeYellow)
}