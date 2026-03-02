package aethex.matrix.ui

import aethex.matrix.R
import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.typography.XFont
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 页头
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.13
 */
object XHeader {
    /**
     * 空页头
     *
     * @param headerPadding 页头内边距
     * @param horizontalArrangement 页头水平对齐方式
     * @param verticalAlignment 页头垂直对齐方式
     * @param content 页头内容
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun Empty(
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = headerPadding.start.dp,
                    top = headerPadding.top.dp,
                    end = headerPadding.end.dp,
                    bottom = headerPadding.bottom.dp
                ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            content()
        }
    }

    /**
     * 图标文字页头
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param text 文字
     * @param textColor 文字颜色
     * @param fontSize 文字大小
     * @param fontWeight 文字粗细
     * @param fontFamily 文字字体
     * @param iconTextPadding 图标文字间距
     * @param headerPadding 页头内边距
     * @param horizontalArrangement 页头水平对齐方式
     * @param verticalAlignment 页头垂直对齐方式
     * @param content 页头内容
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun IconText(
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 30,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 24,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        iconTextPadding: XPadding = XPadding.all(5),
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = headerPadding.start.dp,
                    top = headerPadding.top.dp,
                    end = headerPadding.end.dp,
                    bottom = headerPadding.bottom.dp
                ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconColor != null) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = text,
                        modifier = Modifier
                            .size(iconSize.dp),
                        tint = iconColor
                    )
                } else {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = text,
                        modifier = Modifier
                            .size(iconSize.dp)
                    )
                }
                Spacer(modifier = Modifier.width(iconTextPadding.start.dp))
                Text(
                    text = text,
                    fontSize = fontSize.sp,
                    fontWeight = fontWeight,
                    color = textColor,
                    fontFamily = fontFamily,
                    maxLines = 1
                )
            }

            content()
        }
    }

    @Composable
    fun IconText(
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 30,
        text: Int,
        textColor: Color = Color.White,
        fontSize: Int = 24,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        iconTextPadding: XPadding = XPadding.all(5),
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {}
    ) {
        IconText(
            icon = icon,
            iconColor = iconColor,
            iconSize = iconSize,
            text = stringResource(id = text),
            textColor = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            iconTextPadding = iconTextPadding,
            headerPadding = headerPadding,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }

    /**
     * 图标文字页头
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param text 文字
     * @param textColor 文字颜色
     * @param fontSize 文字大小
     * @param fontWeight 文字粗细
     * @param fontFamily 文字字体
     * @param iconTextPadding 图标文字间距
     * @param headerPadding 页头内边距
     * @param horizontalArrangement 页头水平对齐方式
     * @param verticalAlignment 页头垂直对齐方式
     * @param content 页头内容
     * @param modifier 页头修饰
     *
     * Created by Wu Qizhen on 2026.1.24
     */
    @Composable
    fun IconText(
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 30,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 24,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        iconTextPadding: XPadding = XPadding.all(5),
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {},
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = headerPadding.start.dp,
                    top = headerPadding.top.dp,
                    end = headerPadding.end.dp,
                    bottom = headerPadding.bottom.dp
                ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconColor != null) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = text,
                        modifier = Modifier
                            .size(iconSize.dp),
                        tint = iconColor
                    )
                } else {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = text,
                        modifier = Modifier
                            .size(iconSize.dp)
                    )
                }
                Spacer(modifier = Modifier.width(iconTextPadding.start.dp))
                Text(
                    text = text,
                    fontSize = fontSize.sp,
                    fontWeight = fontWeight,
                    color = textColor,
                    fontFamily = fontFamily,
                    maxLines = 1
                )
            }

            content()
        }
    }

    /**
     * 返回文字页头
     *
     * @param text 文字
     * @param textColor 文字颜色
     * @param fontSize 文字大小
     * @param fontWeight 文字粗细
     * @param fontFamily 文字字体
     * @param iconSize 图标大小
     * @param iconColor 图标颜色
     * @param iconTextPadding 图标文字间距
     * @param headerPadding 页头内边距
     * @param horizontalArrangement 页头水平对齐方式
     * @param verticalAlignment 页头垂直对齐方式
     * @param content 页头内容
     * Created by Wu Qizhen on 2026.1.19
     */
    @Composable
    fun BackText(
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 24,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        headerHeight: Int = 30,
        iconSize: Int = 20,
        iconColor: Color = Color.White,
        iconTextPadding: XPadding = XPadding.all(5),
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = headerPadding.start.dp,
                    top = headerPadding.top.dp,
                    end = headerPadding.end.dp,
                    bottom = headerPadding.bottom.dp
                ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed = interactionSource.collectIsPressedAsState()

                Row(
                    modifier = Modifier
                        .height(headerHeight.dp)
                        .clickVfx(
                            interactionSource = interactionSource
                        ) {
                            (context as? ComponentActivity)?.finish()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AnimatedVisibility(
                        visible = isPressed.value,
                        enter = expandHorizontally() + fadeIn(),
                        exit = shrinkHorizontally() + fadeOut()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(iconSize.dp),
                            tint = iconColor
                        )
                    }

                    Spacer(modifier = Modifier.width(iconTextPadding.start.dp))

                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = fontWeight,
                        fontFamily = fontFamily,
                        maxLines = 1,
                        color = textColor
                    )
                }
            }

            content()
        }
    }

    @Composable
    fun BackText(
        text: Int,
        textColor: Color = Color.White,
        fontSize: Int = 24,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME,
        headerHeight: Int = 30,
        iconSize: Int = 20,
        iconColor: Color = Color.White,
        iconTextPadding: XPadding = XPadding.all(5),
        headerPadding: XPadding = XPadding.horizontal(20).vertical(10),
        horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable () -> Unit = {}
    ) {
        BackText(
            text = stringResource(id = text),
            textColor = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            headerHeight = headerHeight,
            iconSize = iconSize,
            iconColor = iconColor,
            iconTextPadding = iconTextPadding,
            headerPadding = headerPadding,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}