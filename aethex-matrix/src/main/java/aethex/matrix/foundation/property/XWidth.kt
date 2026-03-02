package aethex.matrix.foundation.property

import androidx.compose.ui.unit.dp

/**
 * 粗细系统
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.11.24
 */
object XWidth {
    /**
     * 基础标准粗细
     *
     * Created by Wu Qizhen on 2024.11.24
     * Updated by Wu Qizhen on 2025.11.29
     */
    val NONE = 0.dp
    val ULTRA_SMALL = 0.1f.dp
    val EXTRA_SMALL = 0.2f.dp
    val SMALL = 0.5f.dp
    val MEDIUM = 1.dp
    val LARGE = 1.5.dp
    val EXTRA_LARGE = 2.dp
    val ULTRA_LARGE = 2.5.dp

    /**
     * 边框粗细
     *
     * Created by Wu Qizhen on 2024.11.24
     * Updated by Wu Qizhen on 2025.7.21
     */
    val BORDER_M = SMALL // 默认标准
    val BORDER_S = ULTRA_SMALL
    val BORDER_L = MEDIUM
}