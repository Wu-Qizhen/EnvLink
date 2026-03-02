package aethex.matrix.ui

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColors
import aethex.matrix.foundation.property.XPadding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

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
 * Created by Wu Qizhen on 2026.1.30
 */
object XDialog {
    @Composable
    fun Empty(
        margin: XPadding = XPadding.all(20),
        padding: XPadding = XPadding.all(20),
        borderRadius: Int = 20,
        backgroundColor: Color = XColors.BLACK_GRAY,
        dialogAlignment: Alignment = Alignment.Center,
        onDismiss: () -> Unit,
        content: @Composable () -> Unit
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false  // 禁用平台默认宽度限制
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = margin.start.dp,
                        top = margin.top.dp,
                        bottom = margin.bottom.dp,
                        end = margin.end.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(dialogAlignment)
                        .background(
                            backgroundColor,
                            RoundedCornerShape(borderRadius.dp)
                        )
                        .padding(
                            start = padding.start.dp,
                            end = padding.end.dp,
                            top = padding.top.dp,
                            bottom = padding.bottom.dp
                        )
                ) {
                    content()
                }
            }
        }
    }

    @Composable
    fun Confirm(
        margin: XPadding = XPadding.all(20),
        padding: XPadding = XPadding.all(20),
        borderRadius: Int = 20,
        backgroundColor: Color = XColors.BLACK_GRAY,
        dialogAlignment: Alignment = Alignment.Center,
        optionArrangement: Arrangement.Horizontal = Arrangement.End,
        onDismiss: () -> Unit,
        titleContent: @Composable () -> Unit,
        messageContent: @Composable () -> Unit,
        optionContent: @Composable () -> Unit
    ) {
        this.Empty(
            margin = margin,
            padding = padding,
            borderRadius = borderRadius,
            backgroundColor = backgroundColor,
            dialogAlignment = dialogAlignment,
            onDismiss = onDismiss
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(padding.top.dp)
            ) {
                titleContent()

                messageContent()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = optionArrangement
                ) {
                    optionContent()
                }
            }
        }
    }

    @Composable
    fun Confirm(
        margin: XPadding = XPadding.all(20),
        padding: XPadding = XPadding.all(20),
        borderRadius: Int = 20,
        backgroundColor: Color = XColors.BLACK_GRAY,
        dismissButtonColor: Color? = XColors.PALE_BLUE,
        confirmButtonColor: Color? = XColors.PALE_RED,
        dialogAlignment: Alignment = Alignment.Center,
        optionArrangement: Arrangement.Horizontal = Arrangement.End,
        title: String = "确认操作",
        message: String = "此操作将无法撤销，请确认是否继续？",
        dismiss: String = "取消",
        confirm: String = "确定",
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ) {
        this.Confirm(
            margin = margin,
            padding = padding,
            borderRadius = borderRadius,
            backgroundColor = backgroundColor,
            dialogAlignment = dialogAlignment,
            optionArrangement = optionArrangement,
            onDismiss = onDismiss,
            titleContent = {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            messageContent = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            },
            optionContent = {
                XItem.Button(
                    text = dismiss,
                    color = XColorGroup(
                        background = dismissButtonColor,
                        content = Color.White
                    ),
                    onClick = onDismiss
                )
                Spacer(modifier = Modifier.width(10.dp))
                XItem.Button(
                    text = confirm,
                    color = XColorGroup(
                        background = confirmButtonColor,
                        content = Color.White
                    ),
                    onClick = onConfirm
                )
            }
        )
    }
}