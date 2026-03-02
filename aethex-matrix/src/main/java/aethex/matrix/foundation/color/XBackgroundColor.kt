package aethex.matrix.foundation.color

/**
 * 背景颜色
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
object XBackgroundColor {
    /**
     * ▼ 深色主题背景颜色
     * 组件默认为该主题（暗色）
     *
     * Created by Wu Qizhen on 2024.7.2
     * Updated by Wu Qizhen on 2025.7.22
     */
    val DEFAULT_DARK by lazy { XColors.BG_DARK_M }
    val DEFAULT_DARK_PRESSED by lazy { XColors.BG_DARK_M.withAlpha(0.5f) }
    val THEME_DARK by lazy { XThemeColor.DARKER }
    val THEME_DARK_PRESSED by lazy { XThemeColor.DARKER.withAlpha(0.8f) }
    val RED_DARK by lazy { XColors.RED.getDarkest() }
    val RED_DARK_PRESSED by lazy { XColors.RED.getDarkest().withAlpha(0.8f) }
    val ORANGE_DARK by lazy { XColors.ORANGE.getDarkest() }
    val ORANGE_DARK_PRESSED by lazy { XColors.ORANGE.getDarkest().withAlpha(0.8f) }
    val YELLOW_DARK by lazy { XColors.YELLOW.getDarkest() }
    val YELLOW_DARK_PRESSED by lazy { XColors.YELLOW.getDarkest().withAlpha(0.8f) }
    val GREEN_DARK by lazy { XColors.GREEN.getDarkest() }
    val GREEN_DARK_PRESSED by lazy { XColors.GREEN.getDarkest().withAlpha(0.8f) }
    val BLUE_DARK by lazy { XColors.BLUE.getDarkest() }
    val BLUE_DARK_PRESSED by lazy { XColors.BLUE.getDarkest().withAlpha(0.8f) }
    val PURPLE_DARK by lazy { XColors.PURPLE.getDarkest() }
    val PURPLE_DARK_PRESSED by lazy { XColors.PURPLE.getDarkest().withAlpha(0.8f) }

    /**
     * ▼ 浅色主题背景颜色
     *
     * Created by Wu Qizhen on 2024.7.2
     * Updated by Wu Qizhen on 2025.7.22
     */
    val DEFAULT_LIGHT by lazy { XColors.BG_LIGHT_M }
    val DEFAULT_LIGHT_PRESSED by lazy { XColors.BG_LIGHT_M.withAlpha(0.5f) }
    val THEME_LIGHT by lazy { XThemeColor.BASE }
    val THEME_LIGHT_PRESSED by lazy { XThemeColor.BASE.withAlpha(0.8f) }
    val RED_LIGHT by lazy { XColors.RED.base }
    val RED_LIGHT_PRESSED by lazy { XColors.RED.base.withAlpha(0.8f) }
    val ORANGE_LIGHT by lazy { XColors.ORANGE.base }
    val ORANGE_LIGHT_PRESSED by lazy { XColors.ORANGE.base.withAlpha(0.8f) }
    val YELLOW_LIGHT by lazy { XColors.YELLOW.base }
    val YELLOW_LIGHT_PRESSED by lazy { XColors.YELLOW.base.withAlpha(0.8f) }
    val GREEN_LIGHT by lazy { XColors.GREEN.base }
    val GREEN_LIGHT_PRESSED by lazy { XColors.GREEN.base.withAlpha(0.8f) }
    val BLUE_LIGHT by lazy { XColors.BLUE.base }
    val BLUE_LIGHT_PRESSED by lazy { XColors.BLUE.base.withAlpha(0.8f) }
    val PURPLE_LIGHT by lazy { XColors.PURPLE.base }
    val PURPLE_LIGHT_PRESSED by lazy { XColors.PURPLE.base.withAlpha(0.8f) }

    /*val GRAY by lazy { Color(38, 38, 38, 115)
    val GRAY_PRESSED by lazy { Color(38, 38, 38, 153)
    val RED by lazy { Color(64, 41, 41)
    val RED_PRESSED by lazy { Color(64, 41, 41, 204)
    val GREEN by lazy { Color(37, 54, 39)
    val GREEN_PRESSED by lazy { Color(37, 54, 39, 204)*/
}