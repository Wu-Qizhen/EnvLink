package aethex.matrix.ui

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColors
import aethex.matrix.foundation.color.withAlpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 备注
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.11.30
 * Updated by Wu Qizhen on 2026.1.16
 */
object XNotes {
    /**
     * 成功备注
     *
     * @param title 标题
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Success(
        modifier: Modifier = Modifier,
        title: String?,
        content: String,
        titleSize: Int = 20,
        contentSize: Int = 14
    ) {
        this.Text(
            title = title,
            content = content,
            backgroundColor = XBackgroundColor.GREEN_DARK,
            lineColor = XColors.GREEN.base,
            titleSize = titleSize,
            contentSize = contentSize,
            modifier = modifier
        )
    }

    /**
     * 错误备注
     *
     * @param title 标题
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Error(
        modifier: Modifier = Modifier,
        title: String?,
        content: String,
        titleSize: Int = 20,
        contentSize: Int = 14
    ) {
        this.Text(
            title = title,
            content = content,
            backgroundColor = XBackgroundColor.RED_DARK.withAlpha(0.8f),
            lineColor = XColors.RED.base,
            titleSize = titleSize,
            contentSize = contentSize,
            modifier = modifier
        )
    }

    /**
     * 警告备注
     *
     * @param title 标题
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Warning(
        modifier: Modifier = Modifier,
        title: String?,
        content: String,
        titleSize: Int = 20,
        contentSize: Int = 14
    ) {
        this.Text(
            title = title,
            content = content,
            backgroundColor = XBackgroundColor.YELLOW_DARK.withAlpha(0.8f),
            lineColor = XColors.YELLOW.base,
            titleSize = titleSize,
            contentSize = contentSize,
            modifier = modifier
        )
    }

    /**
     * 信息备注
     *
     * @param title 标题
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Info(
        modifier: Modifier = Modifier,
        title: String?,
        content: String,
        titleSize: Int = 20,
        contentSize: Int = 14
    ) {
        this.Text(
            title = title,
            content = content,
            backgroundColor = XBackgroundColor.BLUE_LIGHT.withAlpha(0.8f),
            lineColor = XColors.BLUE.base,
            titleSize = titleSize,
            contentSize = contentSize,
            modifier = modifier
        )
    }

    /**
     * 备注
     *
     * @param title 标题
     * @param content 内容
     * @param backgroundColor 背景色
     * @param lineColor 线条色
     * @param titleSize 标题大小
     * @param contentSize 内容大小
     * @param modifier 修饰符
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Text(
        title: String?,
        content: String,
        backgroundColor: Color = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
        lineColor: Color = XColors.BLUE.base,
        titleSize: Int = 20,
        contentSize: Int = 14,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .clickVfx(onClick = onClick)
                .height(IntrinsicSize.Min)
                .background(color = backgroundColor, RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(lineColor)
                    .align(Alignment.CenterStart)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        fontSize = titleSize.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                }

                Text(
                    text = content,
                    fontSize = contentSize.sp
                )
            }
        }
    }

    /**
     * 备注
     *
     * @param title 标题
     * @param content 内容
     * @param color 背景色
     * @param titleSize 标题大小
     * @param contentSize 内容大小
     * @param modifier 修饰符
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Text(
        title: String?,
        content: String,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
            border = XColors.BLUE.base
        ),
        titleSize: Int = 20,
        contentSize: Int = 14,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .clickVfx(onClick = onClick)
                .height(IntrinsicSize.Min)
                .background(
                    color = color.background ?: XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
                    RoundedCornerShape(15.dp)
                )
                .clip(RoundedCornerShape(15.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(color.border ?: XColors.BLUE.base)
                    .align(Alignment.CenterStart)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        fontSize = titleSize.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                }

                Text(
                    text = content,
                    fontSize = contentSize.sp
                )
            }
        }
    }

    /**
     * 自定义备注
     *
     * @param backgroundColor 背景色
     * @param lineColor 线条色
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Empty(
        backgroundColor: Color = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
        lineColor: Color = XColors.BLUE.base,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .clickVfx(
                    onClick = onClick
                )
                // .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(color = backgroundColor, RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(lineColor)
                    .align(Alignment.CenterStart)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                content()
            }
        }
    }

    @Composable
    fun Empty(
        modifier: Modifier = Modifier,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
            border = XColors.BLUE.base
        ),
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .clickVfx(
                    onClick = onClick
                )
                // .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(
                    color = color.background ?: XBackgroundColor.BLUE_LIGHT.withAlpha(0.2f),
                    RoundedCornerShape(15.dp)
                )
                .clip(RoundedCornerShape(15.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(color.border ?: XColors.BLUE.base)
                    .align(Alignment.CenterStart)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                content()
            }
        }
    }
}