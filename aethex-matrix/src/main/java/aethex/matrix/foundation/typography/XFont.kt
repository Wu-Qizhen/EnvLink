package aethex.matrix.foundation.typography

import aethex.matrix.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * 字体
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.7.22
 */
object XFont {
    /**
     * ▲ 字体
     *
     * Created by Wu Qizhen on 2025.7.22
     */
    val THEME = FontFamily(Font(R.font.dingtalkjinbuti_regular, FontWeight.Normal))
    val GENERAL_ZH = FontFamily(Font(R.font.misans_regular, FontWeight.Normal))
    val GENERAL_EN = FontFamily(Font(R.font.dingtalkjinbuti_regular, FontWeight.Normal))
}