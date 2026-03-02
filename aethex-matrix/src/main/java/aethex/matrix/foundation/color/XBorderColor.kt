package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 边框颜色
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.7.2
 * Updated by Wu Qizhen on 2025.7.22
 */
object XBorderColor {
    /**
     * ▼ 边框颜色
     *
     * Created by Wu Qizhen on 2024.7.2
     * Updated by Wu Qizhen on 2025.7.22
     */
    val GRAY_GRADIENT = listOf(
        XColors.LINE_DARK_M,
        Color.Transparent
    )
    val GRAY by lazy { XColors.LINE_DARK_M }
}