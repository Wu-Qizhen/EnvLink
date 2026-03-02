package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 颜色系统
 * 所有颜色都应遵循该标准
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.7.26
 * Refactored by Wu Qizhen on 2025.11.28
 */
data class XColor(
    val lightest: Color? = null, // 最浅
    val lighter: Color? = null,  // 较浅
    val light: Color? = null,    // 浅
    val base: Color,             // 基准 / 标准
    val dark: Color? = null,     // 深
    val darker: Color? = null,   // 较深
    val darkest: Color? = null   // 最深
) {
    companion object {
        /**
         * 创建 XColor，自动处理未设置的浅色和深色
         *
         * @param darkest 最深色，如果为 null 则自动生成
         * @param darker 较深色，如果为 null 则自动生成
         * @param dark 深色，如果为 null 则自动生成
         * @param base 基准色（必须设置）
         * @param light 浅色，如果为 null 则设置为 base
         * @param lighter 较浅色，如果为 null 则设置为 base
         * @param lightest 最浅色，如果为 null 则设置为 base
         *
         * Created by Wu Qizhen on 2025.11.28
         */
        fun create(
            lightest: Color? = null,
            lighter: Color? = null,
            light: Color? = null,
            base: Color,
            dark: Color? = null,
            darker: Color? = null,
            darkest: Color? = null
        ): XColor {
            // 浅色处理：如果未设置则使用 base
            val finalLight = light ?: base
            val finalLighter = lighter ?: base
            val finalLightest = lightest ?: base

            // 深色处理：如果未设置则通过 base + 透明度生成
            val finalDark = dark ?: base.withAlpha(0.7f)
            val finalDarker = darker ?: base.withAlpha(0.5f)
            val finalDarkest = darkest ?: base.withAlpha(0.3f)

            return XColor(
                lightest = finalLightest,
                lighter = finalLighter,
                light = finalLight,
                base = base,
                dark = finalDark,
                darker = finalDarker,
                darkest = finalDarkest
            )
        }
    }

    /**
     * 安全获取颜色
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    fun getDarkest(): Color = darkest ?: base.withAlpha(0.3f)
    fun getDarker(): Color = darker ?: base.withAlpha(0.5f)
    fun getDark(): Color = dark ?: base.withAlpha(0.7f)
    fun getLight(): Color = light ?: base
    fun getLighter(): Color = lighter ?: base
    fun getLightest(): Color = lightest ?: base
}

/**
 * 为 Color 添加透明度
 *
 * Created by Wu Qizhen on 2025.11.28
 */
fun Color.withAlpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}