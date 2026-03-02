package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

/**
 * 主题颜色
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.7.22
 * Refactored by Wu Qizhen on 2025.11.28
 */
object XThemeColor {
    /**
     * 主题颜色
     *
     * Created by Wu Qizhen on 2025.7.22
     * Deprecated on 2025.11.28
     */
    /*data class ThemeColors(
        val light: Color,
        val base: Color,
        val dark: Color,
        val darker: Color
    )*/

    /**
     * 预设主题原色
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    private val THEME_RED = XColors.RED
    private val THEME_ORANGE = XColors.ORANGE
    private val THEME_YELLOW = XColors.YELLOW
    private val THEME_GREEN = XColors.GREEN
    private val THEME_BLUE = XColors.BLUE
    private val THEME_PURPLE = XColors.PURPLE

    /**
     * ▲ 主题颜色
     * 使用预设或通过主题颜色生成色阶
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    // val THEME_COLORS = generateThemeColors(Color(255, 255, 255, 255)) // 不建议使用动态生成方式
    private val THEME_COLORS: XColor = THEME_PURPLE

    /**
     * 通过主题颜色生成色阶
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    private fun generateThemeColors(themeColor: Color): XColor {
        // 将 Compose 的 Color 转换为 HSL 表示
        val hsl = rgbToHsl(themeColor)
        val (h, s, l) = hsl

        // 计算浅色版本（提高亮度，降低饱和度）
        val lightL = min(0.93f, l * 1.4f)
        val lightS = max(0.4f, s * 0.7f)
        val lightColor = hslToRgb(h, lightS, lightL)

        // 计算深色版本（降低亮度，增加饱和度）
        val deepL = max(0.25f, l * 0.65f)
        val deepS = min(0.95f, s * 1.1f)
        val deepColor = hslToRgb(h, deepS, deepL)

        // 计算更深色版本（大幅降低亮度）
        val darkL = max(0.15f, l * 0.35f)
        val darkS = s * 0.85f
        val darkColor = hslToRgb(h, darkS, darkL)

        return XColor(
            light = lightColor,
            base = themeColor,
            dark = deepColor,
            darker = darkColor
        )
    }

    /**
     * 将 RGB 颜色转换为 HSL 表示
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    private fun rgbToHsl(color: Color): Triple<Float, Float, Float> {
        val r = color.red
        val g = color.green
        val b = color.blue

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        var h: Float
        val l = (max + min) / 2

        if (max == min) {
            h = 0f // 无色相（灰度）
        } else {
            val d = max - min
            h = when (max) {
                r -> ((g - b) / d + (if (g < b) 6 else 0)) * 60
                g -> ((b - r) / d + 2) * 60
                else -> ((r - g) / d + 4) * 60
            }
            h %= 360
            if (h < 0) h += 360
        }

        val s = if (max == 0f || min == 1f) {
            0f
        } else {
            (max - min) / (1 - kotlin.math.abs(2 * l - 1))
        }

        return Triple(h, s, l)
    }

    /**
     * 将 HSL 颜色表示转换为 RGB
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    private fun hslToRgb(h: Float, s: Float, l: Float): Color {
        val c = (1 - kotlin.math.abs(2 * l - 1)) * s
        val x = c * (1 - kotlin.math.abs((h / 60) % 2 - 1))
        val m = l - c / 2

        val (r, g, b) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color(
            red = (r + m).coerceIn(0f, 1f),
            green = (g + m).coerceIn(0f, 1f),
            blue = (b + m).coerceIn(0f, 1f)
        )
    }

    /**
     * 主题颜色
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    val BASE by lazy { THEME_COLORS.base }
    val LIGHT by lazy { THEME_COLORS.getLight() }
    val DARK by lazy { THEME_COLORS.getDark() }
    val DARKER by lazy { THEME_COLORS.getDarker() }
    /*fun normal(): Color {
        return THEME_COLORS.normal
    }*/
}