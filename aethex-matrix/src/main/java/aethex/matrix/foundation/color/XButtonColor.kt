package aethex.matrix.foundation.color

import aethex.matrix.foundation.type.XButtonType
import aethex.matrix.foundation.type.XColorType

/**
 * 类型
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.12.22
 * Updated by Wu Qizhen on 2025.7.22
 * Updated by Wu Qizhen on 2026.1.12
 */
object XButtonColor {
    /**
     * 危险类型
     *
     * Created by Wu Qizhen on 2024.12.22
     * Updated by Wu Qizhen on 2025.7.22
     */
    val DANGER_DARK = XColorGroup(
        background = XBackgroundColor.RED_DARK,
        activeBackground = XBackgroundColor.RED_DARK_PRESSED,
        content = XContentColor.RED_DARK
    )
    val DANGER_LIGHT = XColorGroup(
        background = XBackgroundColor.RED_LIGHT,
        activeBackground = XBackgroundColor.RED_LIGHT_PRESSED,
        content = XColors.WHITE.base
    )

    /**
     * 警告类型
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val WARNING_DARK = XColorGroup(
        background = XBackgroundColor.YELLOW_DARK,
        activeBackground = XBackgroundColor.YELLOW_DARK_PRESSED,
        content = XContentColor.YELLOW_DARK
    )
    val WARNING_LIGHT = XColorGroup(
        background = XBackgroundColor.YELLOW_LIGHT,
        activeBackground = XBackgroundColor.YELLOW_LIGHT_PRESSED,
        content = XColors.WHITE.base
    )

    /**
     * 安全类型
     *
     * Created by Wu Qizhen on 2024.12.22
     * Updated by Wu Qizhen on 2025.7.22
     */
    val SAFE_DARK = XColorGroup(
        background = XBackgroundColor.GREEN_DARK,
        activeBackground = XBackgroundColor.GREEN_DARK_PRESSED,
        content = XContentColor.GREEN_DARK
    )
    val SAFE_LIGHT = XColorGroup(
        background = XBackgroundColor.GREEN_LIGHT,
        activeBackground = XBackgroundColor.GREEN_LIGHT_PRESSED,
        content = XColors.WHITE.base
    )

    /**
     * 普通类型
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val PRIMARY_DARK = XColorGroup(
        background = XBackgroundColor.BLUE_DARK,
        activeBackground = XBackgroundColor.BLUE_DARK_PRESSED,
        content = XContentColor.BLUE_DARK
    )
    val PRIMARY_LIGHT = XColorGroup(
        background = XBackgroundColor.BLUE_LIGHT,
        activeBackground = XBackgroundColor.BLUE_LIGHT_PRESSED,
        content = XColors.WHITE.base
    )

    /**
     * 主题类型
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val THEME_DARK = XColorGroup(
        background = XBackgroundColor.THEME_DARK,
        activeBackground = XBackgroundColor.THEME_DARK_PRESSED,
        content = XContentColor.THEME_DARK
    )
    val THEME_LIGHT = XColorGroup(
        background = XBackgroundColor.THEME_LIGHT,
        activeBackground = XBackgroundColor.THEME_LIGHT_PRESSED,
        content = XColors.WHITE.base
    )

    /**
     * 按钮颜色
     *
     * Created by Wu Qizhen on 2025.7.22
     * Deprecated on 2026.1.12
     */
    /*data class ButtonColor(
        val background: Color,
        val backgroundPressed: Color,
        val content: Color
    )*/

    /**
     * 获取按钮颜色配置
     *
     * @param buttonType 按钮类型
     * @param colorType 颜色变体，默认为深色
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    fun getColor(
        buttonType: XButtonType = XButtonType.PRIMARY,
        colorType: XColorType = XColorType.DARK
    ): XColorGroup {
        return when (buttonType) {
            XButtonType.DANGER -> when (colorType) {
                XColorType.DARK -> DANGER_DARK
                XColorType.LIGHT -> DANGER_LIGHT
            }

            XButtonType.WARNING -> when (colorType) {
                XColorType.DARK -> WARNING_DARK
                XColorType.LIGHT -> WARNING_LIGHT
            }

            XButtonType.SAFE -> when (colorType) {
                XColorType.DARK -> SAFE_DARK
                XColorType.LIGHT -> SAFE_LIGHT
            }

            XButtonType.PRIMARY -> when (colorType) {
                XColorType.DARK -> PRIMARY_DARK
                XColorType.LIGHT -> PRIMARY_LIGHT
            }

            XButtonType.THEME -> when (colorType) {
                XColorType.DARK -> THEME_DARK
                XColorType.LIGHT -> THEME_LIGHT
            }
        }
    }
}