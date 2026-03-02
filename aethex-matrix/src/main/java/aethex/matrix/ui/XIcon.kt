package aethex.matrix.ui

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.typography.XFont
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 图标
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.13
 * Updated by Wu Qizhen on 2026.1.16
 */
object XIcon {
    /**
     * 圆形图标
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param planeColor 背景颜色
     * @param planeSize 背景大小
     * @param modifier 修改器
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        icon: Int,
        iconColor: Color? /*= Color.White*/,
        iconSize: Int = 20,
        planeColor: Color? /*= XBackgroundColor.THEME_LIGHT*/,
        planeSize: Int = 30,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(planeSize.dp)
                .background(
                    color = planeColor ?: Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (iconColor != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp),
                    tint = iconColor
                )
            } else {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp)
                )
            }
        }
    }

    @Composable
    fun RoundPlane(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 30,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.THEME_LIGHT,
            content = Color.White
        ),
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val dynamicPlaneColor by animateColorAsState(
            targetValue = if (isPressed.value) (color.activeBackground
                ?: Color.Transparent) else (color.background ?: Color.Transparent)
        )
        val dynamicIconColor by animateColorAsState(
            targetValue = if (isPressed.value) (color.activeContent
                ?: Color.White) else (color.content ?: Color.White)
        )

        Box(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    onClick = onClick
                )
                .size(planeSize.dp)
                .background(
                    color = dynamicPlaneColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (color.content != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp),
                    tint = dynamicIconColor
                )
            } else {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp)
                )
            }
        }
    }

    /**
     * 圆形图标
     *
     * @param modifier 修改器
     * @param icon 图标资源
     * @param iconSize 图标大小
     * @param planeSize 背景大小
     * @param color 颜色
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 30,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.THEME_LIGHT,
            content = Color.White
        )
    ) {
        RoundPlane(
            icon = icon,
            iconColor = color.content,
            iconSize = iconSize,
            planeColor = color.background,
            planeSize = planeSize,
            modifier = modifier
        )
    }

    /**
     * 圆形图标
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param planeColor 背景颜色
     * @param planeSize 背景大小
     * @param modifier 修改器
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        icon: Int,
        iconColor: Color? = Color.White,
        iconSize: Int = 20,
        planeColor: List<Color>,
        planeSize: Int = 30,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(planeSize.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = planeColor
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (iconColor != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp),
                    tint = iconColor
                )
            } else {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize.dp)
                )
            }
        }
    }

    /**
     * 圆形图标
     *
     * @param text 文本
     * @param textColor 文本颜色
     * @param fontSize 文本大小
     * @param fontWeight 文本粗细
     * @param fontFamily 文本字体
     * @param planeColor 背景颜色
     * @param planeSize 背景大小
     * @param modifier 修改器
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 16,
        planeColor: Color? = XBackgroundColor.THEME_LIGHT,
        planeSize: Int = 40,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(planeSize.dp)
                .background(
                    color = planeColor ?: Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                color = textColor,
                maxLines = 1
            )
        }
    }

    /**
     * 圆形图标
     *
     * @param text 文本
     * @param fontSize 文本大小
     * @param fontWeight 文本粗细
     * @param fontFamily 文本字体
     * @param planeSize 背景大小
     * @param color 颜色
     * @param modifier 修改器
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        modifier: Modifier = Modifier,
        text: String,
        fontSize: Int = 16,
        planeSize: Int = 40,
        color: XColorGroup = XColorGroup(
            background = XBackgroundColor.THEME_LIGHT,
            content = Color.White
        ),
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME
    ) {
        RoundPlane(
            text = text,
            textColor = color.content ?: Color.White,
            fontSize = fontSize,
            planeColor = color.background,
            planeSize = planeSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            modifier = modifier
        )
    }

    /**
     * 圆形图标
     *
     * @param text 文本
     * @param textColor 文本颜色
     * @param fontSize 文本大小
     * @param fontWeight 文本粗细
     * @param fontFamily 文本字体
     * @param planeColor 背景颜色
     * @param planeSize 背景大小
     * @param modifier 修改器
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun RoundPlane(
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 16,
        planeColor: List<Color>,
        planeSize: Int = 40,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(planeSize.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = planeColor
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                color = textColor,
                maxLines = 1
            )
        }
    }
}