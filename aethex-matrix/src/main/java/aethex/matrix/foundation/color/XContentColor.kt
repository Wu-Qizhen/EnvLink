package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 内容颜色
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
object XContentColor {
    /**
     * ▼ 深色主题内容颜色
     *
     * Created by Wu Qizhen on 2024.7.2
     * Updated by Wu Qizhen on 2025.7.22
     */
    val RED_DARK by lazy { XColors.RED.getLight() }
    val ORANGE_DARK by lazy { XColors.ORANGE.getLight() }
    val YELLOW_DARK by lazy { XColors.YELLOW.getLight() }
    val GREEN_DARK by lazy { XColors.GREEN.getLight() }
    val BLUE_DARK by lazy { XColors.BLUE.getLight() }
    val PURPLE_DARK by lazy { XColors.PURPLE.getLight() }
    val GRAY_DARK = Color(148, 164, 184)
    val DEFAULT_DARK by lazy { XColors.WHITE.getDark() }
    val THEME_DARK by lazy { XThemeColor.LIGHT }

    /**
     * ▼ 浅色主题内容颜色
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val RED_LIGHT by lazy { XColors.RED.getDark() }
    val ORANGE_LIGHT by lazy { XColors.ORANGE.getDark() }
    val YELLOW_LIGHT by lazy { XColors.YELLOW.getDark() }
    val GREEN_LIGHT by lazy { XColors.GREEN.getDark() }
    val BLUE_LIGHT by lazy { XColors.BLUE.getDark() }
    val PURPLE_LIGHT by lazy { XColors.PURPLE.getDark() }
    val DEFAULT_LIGHT by lazy { XColors.BLACK.getLight() }
    val THEME_LIGHT by lazy { XThemeColor.DARK }
}