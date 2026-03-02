package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 预设颜色
 * 遵循 XColor 标准
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.11.28
 */
object XColors {
    /**
     * 基础标准色
     *
     * Created by Wu Qizhen on 2025.11.28
     */
    val RED = XColor(
        light = Color(214, 77, 91, 255),
        base = Color(239, 0, 39, 255),
        dark = Color(168, 1, 28, 255),
        darker = Color(76, 0, 12, 255),
        darkest = Color(64, 41, 41, 255)
    )
    val ORANGE = XColor(
        light = Color(255, 158, 72, 255),
        base = Color(255, 82, 0, 255),
        dark = Color(199, 42, 1, 255),
        darker = Color(136, 37, 3, 255)
    )
    val YELLOW = XColor(
        light = Color(255, 241, 179, 255),
        base = Color(254, 195, 58, 255),
        dark = Color(199, 160, 37, 255),
        darker = Color(104, 78, 23, 255),
        darkest = Color(75, 75, 54, 255)
    )
    val GREEN = XColor(
        light = Color(82, 224, 166, 255),
        base = Color(61, 220, 132, 255),
        dark = Color(87, 150, 92, 255),
        darker = Color(37, 93, 68, 255),
        darkest = Color(37, 54, 39, 255)
    )
    val BLUE = XColor(
        lightest = Color(41, 218, 255, 255),
        lighter = Color(0, 199, 228, 255),
        light = Color(30, 144, 255, 255),
        base = Color(59, 130, 246, 255),
        dark = Color(10, 83, 242, 255),
        darker = Color(23, 45, 96, 255),
        darkest = Color(9, 31, 44, 255)
    )
    val PURPLE = XColor(
        light = Color(197, 134, 245, 255),
        base = Color(161, 111, 225, 255),
        dark = Color(103, 58, 183, 255),
        darker = Color(47, 41, 54, 255)
    )
    val GRAY = XColor(
        lightest = Color(120, 120, 120, 255),
        lighter = Color(100, 100, 100, 255),
        light = Color(85, 85, 85, 255),
        base = Color(65, 65, 65, 255),
        dark = Color(54, 54, 54, 255),
        darker = Color(45, 45, 45, 255),
        darkest = Color(36, 36, 36, 255)
    )
    val WHITE = XColor(
        base = Color(255, 255, 255, 255),
        dark = Color(200, 200, 200, 255),
        darker = Color(160, 160, 160, 255),
        darkest = Color(120, 120, 120, 255),
    )
    val BLACK = XColor(
        lightest = Color(60, 60, 60, 255),
        lighter = Color(40, 40, 40, 255),
        light = Color(20, 20, 20, 255),
        base = Color(0, 0, 0, 255),
    )
    val BLACK_GRAY = Color(33, 33, 33)
    val PALE_RED = RED.light
    val PALE_BLUE = BLUE.light
    val SKY_BLUE = BLUE.lighter

    /**
     * 深色主题组件背景色
     *
     * Created by Wu Qizhen on 2025.11.28
     */
    val BG_DARK_XXXL by lazy { GRAY.getLightest() }
    val BG_DARK_XXL by lazy { GRAY.getLighter() }
    val BG_DARK_XL by lazy { GRAY.getLight() }
    val BG_DARK_L by lazy { GRAY.getDark() }
    val BG_DARK_M by lazy { GRAY.getDarker() } // 默认标准
    val BG_DARK_D by lazy { GRAY.getDarkest() }

    /**
     * 浅色主题组件背景色
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val BG_LIGHT_L by lazy { GRAY.getLightest() }
    val BG_LIGHT_M by lazy { GRAY.getLighter() } // 默认标准
    val BG_LIGHT_D by lazy { GRAY.getLight() }
    val BG_LIGHT_XD by lazy { GRAY.getDark() }
    val BG_LIGHT_XXD by lazy { GRAY.getDarker() }
    val BG_LIGHT_XXXD by lazy { GRAY.getDarkest() }

    /**
     * 深色主题组件内容色
     *
     * Created by Wu Qizhen on 2025.11.28
     */
    val CONTENT_DARK_L by lazy { WHITE.base }
    val CONTENT_DARK_M by lazy { WHITE.getDarker() } // 默认标准
    val CONTENT_DARK_D by lazy { WHITE.getDarkest() }

    /**
     * 浅色主题组件内容色
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val CONTENT_LIGHT_L by lazy { BLACK.getLightest() }
    val CONTENT_LIGHT_M by lazy { BLACK.getLighter() } // 默认标准
    val CONTENT_LIGHT_D by lazy { BLACK.base }

    /**
     * 深色主题组件线条色
     *
     * Created by Wu Qizhen on 2025.11.28
     */
    val LINE_DARK_L by lazy { GRAY.getLighter() }
    val LINE_DARK_M by lazy { GRAY.getLight() } // 默认标准
    val LINE_DARK_D by lazy { GRAY.base }
    val LINE_DARK_XD by lazy { GRAY.getDark() }
    val LINE_DARK_XXD by lazy { GRAY.getDarker() }
    val LINE_DARK_XXXD by lazy { GRAY.getDarkest() }

    /**
     * 浅色主题组件线条色
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val LINE_LIGHT_XXXL by lazy { GRAY.getLightest() }
    val LINE_LIGHT_XXL by lazy { GRAY.getLighter() }
    val LINE_LIGHT_XL by lazy { GRAY.getLight() }
    val LINE_LIGHT_L by lazy { GRAY.base }
    val LINE_LIGHT_M by lazy { GRAY.getDark() } // 默认标准
    val LINE_LIGHT_D by lazy { GRAY.getDarker() }
}