package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 选项渐变颜色
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.13
 */
data class XColorsGroup(
    val backgroundGradient: List<Color> = listOf(
        XBackgroundColor.DEFAULT_DARK,
        XBackgroundColor.DEFAULT_DARK_PRESSED
    ),
    val activeBackground: List<Color> = backgroundGradient,
    val borderGradient: List<Color> = listOf(Color.Transparent),
    val activeBorder: List<Color> = borderGradient,
    val content: Color = XContentColor.DEFAULT_DARK,
    val activeContent: Color = XContentColor.DEFAULT_DARK,
)