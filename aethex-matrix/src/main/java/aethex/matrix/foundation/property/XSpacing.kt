package aethex.matrix.foundation.property

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 间距系统
 * 所有间距都建议遵循该标准
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.11.29
 */
data class XSpacing(
    val small: Dp? = null,  // 适用于穿戴
    val base: Dp,           // 适用于手机
    val large: Dp? = null,  // 适用于平板
    val larger: Dp? = null, // 适用于桌面
) {
    companion object {
        /**
         * 创建 XSpacing，自动处理未设置的小尺寸和大尺寸
         *
         * @param small 较小间距，如果为 null 则自动生成
         * @param base 基准间距（必须设置）
         * @param large 较大间距，如果为 null 则自动生成
         * @param larger 大间距，如果为 null 则自动生成
         *
         * Created by Wu Qizhen on 2025.11.29
         */
        fun create(
            small: Dp? = null,
            base: Dp,
            large: Dp? = null,
            larger: Dp? = null,
        ): XSpacing {
            val finalSmall = small ?: (if (base.value < 20) base.scaleAndRound(0.5) else base)
            val finalLarge = large ?: (if (base.value < 20) base.scaleAndRound(2.0) else base)
            val finalLarger = larger ?: (if (base.value < 20) base.scaleAndRound(2.0) else base)

            return XSpacing(
                small = finalSmall,
                base = base,
                large = finalLarge,
                larger = finalLarger
            )
        }
    }

    /**
     * 安全获取颜间距
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    fun getSmall(): Dp = small ?: (if (base.value < 20) base.scaleAndRound(0.5) else base)
    fun getLarge(): Dp = large ?: (if (base.value < 20) base.scaleAndRound(2.0) else base)
    fun getLarger(): Dp = larger ?: (if (base.value < 20) base.scaleAndRound(2.0) else base)
}

/**
 * 将 Dp 值按指定倍数缩放并取整
 *
 * Created by Wu Qizhen on 2025.11.29
 */
fun Dp.scaleAndRound(scale: Double): Dp {
    return (this.value * scale).roundToInt().toFloat().dp
}