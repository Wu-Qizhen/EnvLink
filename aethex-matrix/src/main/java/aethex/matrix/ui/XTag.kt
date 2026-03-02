package aethex.matrix.ui

import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColors
import aethex.matrix.foundation.color.withAlpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 标签
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.11
 * Updated by Wu Qizhen on 2026.1.16
 */
object XTag {
    /**
     * 文本标签
     *
     * // @param text 标签文本
     * // @param fontSize 文本大小
     * // @param color 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     * Deprecated on 2026.1.16
     */
    /*@Composable
    fun Text(
        text: String,
        fontSize: Int = 12,
        color: Color = XColors.BLUE.base
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color.withAlpha(0.2f),
                    shape = RoundedCornerShape(
                        4.dp
                    )
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = color,
                maxLines = 1
            )
        }
    }*/

    /**
     * 文本标签
     *
     * @param text 标签文本
     * @param fontSize 文本大小
     * @param textColor 标签颜色
     * @param tagColor 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Text(
        text: String,
        fontSize: Int = 12,
        textColor: Color = XColors.BLUE.base,
        tagColor: Color = textColor.withAlpha(0.2f),
        borderRadius: Int = 4
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = tagColor,
                    shape = RoundedCornerShape(
                        borderRadius.dp
                    )
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                maxLines = 1
            )
        }
    }

    /**
     * 文本标签
     *
     * @param text 标签文本
     * @param fontSize 文本大小
     * @param color 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Text(
        text: String,
        fontSize: Int = 12,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
            content = XColors.BLUE.base
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color.background ?: XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
                    shape = RoundedCornerShape(
                        4.dp
                    )
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = color.content ?: XColors.BLUE.base,
                maxLines = 1
            )
        }
    }

    /**
     * 文本标签
     *
     * // @param text 标签文本
     * // @param fontSize 文本大小
     * // @param color 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     * Deprecated on 2026.1.16
     */
    /*@Composable
    fun Text(
        text: Int,
        fontSize: Int = 12,
        color: Color = XColors.BLUE.base
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = color.withAlpha(0.2f),
                    shape = RoundedCornerShape(
                        4.dp
                    )
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = stringResource(id = text),
                fontSize = fontSize.sp,
                color = color,
                maxLines = 1
            )
        }
    }*/

    /**
     * 文本标签
     *
     * @param text 标签文本
     * @param fontSize 文本大小
     * @param textColor 标签颜色
     * @param tagColor 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Text(
        text: Int,
        fontSize: Int = 12,
        textColor: Color = XColors.BLUE.base,
        tagColor: Color = textColor.withAlpha(0.2f),
        borderRadius: Int = 4
    ) {
        this.Text(
            text = stringResource(id = text),
            fontSize = fontSize,
            textColor = textColor,
            tagColor = tagColor,
            borderRadius = borderRadius
        )
    }

    /**
     * 文本标签
     *
     * @param text 标签文本
     * @param fontSize 文本大小
     * @param color 标签颜色
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Text(
        text: Int,
        fontSize: Int = 12,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
            content = XColors.BLUE.base
        )
    ) {
        this.Text(
            text = stringResource(id = text),
            fontSize = fontSize,
            color = color
        )
    }
}