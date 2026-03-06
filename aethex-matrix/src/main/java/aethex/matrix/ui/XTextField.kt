package aethex.matrix.ui

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColors
import aethex.matrix.foundation.color.XThemeColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

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
 * Created by Wu Qizhen on 2026.2.7
 */
object XTextField {
    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: @Composable () -> Unit = {},
        color: XColorGroup = XColorGroup(
            background = XColors.BG_DARK_M,
            activeBackground = Color.Transparent,
            content = Color.White,
            activeContent = XThemeColor.BASE,
            border = Color.Transparent,
            activeBorder = XThemeColor.BASE
        ),
        borderRadius: Int = 15,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()

        // 动画边框颜色变化
        val borderColor by animateColorAsState(
            targetValue = if (isFocused) (color.activeBorder ?: XThemeColor.BASE) else (color.border
                ?: Color.Transparent),
            label = "borderColor"
        )
        val labelColor by animateColorAsState(
            targetValue = if (value.isNotEmpty()) (color.activeContent
                ?: XThemeColor.BASE) else (color.content
                ?: Color.White),
            label = "labelColor"
        )

        TextField(
            label = {
                Text(
                    text = label,
                    color = labelColor
                )
            },
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                placeholder()
            },
            modifier = modifier
                .heightIn(min = 60.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(borderRadius.dp)
                ),
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(borderRadius.dp),
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = color.activeBackground ?: Color.Transparent,
                unfocusedContainerColor = color.background ?: XColors.BG_DARK_M,
                focusedLabelColor = color.activeBorder ?: XThemeColor.BASE,
                unfocusedLabelColor = Color.White,
                cursorColor = color.activeBorder ?: XThemeColor.BASE
            )
        )
    }

    @Composable
    fun Outline(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: @Composable () -> Unit = {},
        color: XColorGroup = XColorGroup(
            background = XColors.BG_DARK_M,
            activeBackground = Color.Transparent,
            content = Color.White,
            border = Color.Transparent,
            activeBorder = XThemeColor.BASE
        ),
        borderRadius: Int = 15,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
    ) {
        OutlinedTextField(
            label = {
                Text(
                    text = label,
                    color = color.content ?: Color.White
                )
            },
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                placeholder()
            },
            modifier = modifier,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(borderRadius.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = color.activeBackground ?: Color.Transparent,
                unfocusedContainerColor = color.background ?: XColors.BG_DARK_M,
                focusedLabelColor = color.activeBorder ?: XThemeColor.BASE,
                unfocusedLabelColor = Color.White,
                cursorColor = color.activeBorder ?: XThemeColor.BASE,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }

    @Composable
    fun Outline(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: @Composable () -> Unit = {},
        color: XColorGroup = XColorGroup(
            background = XColors.BG_DARK_M,
            activeBackground = Color.Transparent,
            content = Color.White,
            border = Color.Transparent,
            activeBorder = XThemeColor.BASE
        ),
        borderRadius: Int = 15,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions
    ) {
        OutlinedTextField(
            label = {
                Text(
                    text = label,
                    color = color.content ?: Color.White
                )
            },
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                placeholder()
            },
            textStyle = TextStyle(
                color = color.content ?: Color.White, // 会覆盖 focusedTextColor / unfocusedTextColor
            ),
            modifier = modifier,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(borderRadius.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = color.activeBackground ?: Color.Transparent,
                unfocusedContainerColor = color.background ?: XColors.BG_DARK_M,
                focusedLabelColor = color.activeBorder ?: XThemeColor.BASE,
                unfocusedLabelColor = color.content ?: Color.White,
                cursorColor = color.activeBorder ?: XThemeColor.BASE,
                focusedIndicatorColor = color.activeBorder ?: XThemeColor.BASE,
                unfocusedIndicatorColor = color.activeBorder ?: XThemeColor.BASE,
                focusedTextColor = color.content ?: Color.White,
                unfocusedTextColor = color.content ?: Color.White
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}