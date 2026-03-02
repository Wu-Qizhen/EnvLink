package aethex.matrix.foundation.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 版面设计
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.7.3
 * Updated by Wu Qizhen on 2025.7.22
 */
object XTypography {
    /**
     * ▼ 默认字体
     *
     * Created by Wu Qizhen on 2025.7.3
     * Updated by Wu Qizhen on 2025.7.22
     */
    val TYPOGRAPHY = Typography(
        bodyLarge = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.White,
            // lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.White,
            // lineHeight = 20.sp,
            letterSpacing = 0.5.sp
        ),
        bodySmall = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color.White,
            // lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            // lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleMedium = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White,
            // lineHeight = 20.sp,
            letterSpacing = 0.5.sp
        ),
        titleSmall = TextStyle(
            fontFamily = XFont.GENERAL_ZH,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White,
            // lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}