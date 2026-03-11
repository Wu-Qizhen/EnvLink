package com.codeintellix.envlink.entity.protocol

import androidx.compose.ui.graphics.Color
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeYellow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.11
 */
enum class EnvironmentState(val description: String, val color: Color) {
    UNKNOWN("环境未知", Gray),
    GOOD("环境良好", LightGreen),
    WARN("需要注意", OrangeYellow)
}