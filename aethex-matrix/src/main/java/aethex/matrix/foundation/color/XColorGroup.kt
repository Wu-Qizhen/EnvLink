package aethex.matrix.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * 选项颜色
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.12
 */
data class XColorGroup(
    val background: Color? = XBackgroundColor.DEFAULT_DARK,
    val activeBackground: Color? = background?.withAlpha(0.3f),
    val border: Color? = Color.Transparent,
    val activeBorder: Color? = border,
    val content: Color? = XContentColor.DEFAULT_DARK,
    val activeContent: Color? = XContentColor.DEFAULT_DARK
)