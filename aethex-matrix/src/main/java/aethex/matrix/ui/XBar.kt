@file:OptIn(ExperimentalSharedTransitionApi::class)

package aethex.matrix.ui

import aethex.matrix.R
import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColors
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.property.XSpacings
import aethex.matrix.foundation.property.XStats
import aethex.matrix.foundation.type.XAlignType
import aethex.matrix.foundation.type.XSpacingType
import aethex.matrix.foundation.typography.XFont
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 栏条
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.6.23
 * Refactored by Wu Qizhen on 2024.11.30
 * Updated by Wu Qizhen on 2025.7.23
 */
object XBar {
    /**
     * ▼ 文本标题栏
     *
     * @param title 标题
     * @param enabledThemeColor 是否启用主题色
     * @param fontSize 文字大小
     *
     * Created by Wu Qizhen on 2024.6.23
     * Refactored by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Text(
        modifier: Modifier = Modifier,
        // align: XAlignType = XAlignType.CENTER,
        title: String = "主页",
        fontSize: Int = 20,
        enabledThemeColor: Boolean = true
    ) {
        val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()

        Row(
            modifier = modifier
                .height((fontSize * 2).dp)
                .clickVfx(
                    interactionSource = interactionSource
                ) {
                    (context as? ComponentActivity)?.finish()
                },
            verticalAlignment = Alignment.CenterVertically,
            /*horizontalArrangement = if (XStats.isScreenRound()) Arrangement.Center else when (align) {
                XAlignType.START -> Arrangement.Start
                XAlignType.CENTER -> Arrangement.Center
                XAlignType.END -> Arrangement.End
            }*/
        ) {
            AnimatedVisibility(
                visible = isPressed.value,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.size((fontSize - 2).dp),
                    tint = if (enabledThemeColor) XThemeColor.BASE else XColors.WHITE.base
                )
            }

            Text(
                text = title,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Thin,
                fontFamily = XFont.THEME,
                maxLines = 1,
                color = if (enabledThemeColor) XThemeColor.BASE else XColors.WHITE.base,
                textAlign = TextAlign.Center
            )
        }
    }

    /**
     * ▼ 文本标题栏
     *
     * @param title 标题资源
     * @param align 对齐方式
     * @param enabledThemeColor 是否启用主题色
     * @param fontSize 文字大小
     *
     * Created by Wu Qizhen on 2024.6.23
     * Refactored by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Text(
        title: Int,
        align: XAlignType = XAlignType.CENTER,
        fontSize: Int = 20,
        enabledThemeColor: Boolean = true
    ) {
        val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height((fontSize * 2).dp)
                .clickVfx(
                    interactionSource = interactionSource
                ) {
                    (context as? ComponentActivity)?.finish()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (XStats.isScreenRound()) Arrangement.Center else when (align) {
                XAlignType.START -> Arrangement.Start
                XAlignType.CENTER -> Arrangement.Center
                XAlignType.END -> Arrangement.End
            }
            // horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = isPressed.value,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.size((fontSize - 2).dp),
                    tint = if (enabledThemeColor) XThemeColor.BASE else XColors.WHITE.base
                )
            }

            Text(
                text = stringResource(id = title),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Thin,
                fontFamily = XFont.THEME,
                maxLines = 1,
                color = if (enabledThemeColor) XThemeColor.BASE else XColors.WHITE.base,
                textAlign = TextAlign.Center
            )
        }
    }

    /**
     * 徽标标题栏
     *
     * @param logo 徽标资源
     * @param title 标题资源
     * @param align 对齐方式
     * @param contentHeight 内容高度
     *
     * Created by Wu Qizhen on 2024.6.23
     * Refactored by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Logo(
        logo: Int,
        title: Int,
        align: XAlignType = XAlignType.CENTER,
        contentHeight: Int = 25
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickVfx(),
            horizontalAlignment = if (XStats.isScreenRound()) Alignment.CenterHorizontally else when (align) {
                XAlignType.START -> Alignment.Start
                XAlignType.CENTER -> Alignment.CenterHorizontally
                XAlignType.END -> Alignment.End
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 25.dp,
                        bottom = XSpacings.getComponentSpacing()
                    )
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(contentHeight.dp),
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = painterResource(id = title),
                    modifier = Modifier
                        .height(contentHeight.dp)
                        .wrapContentWidth() // .width(100.dp)
                    ,
                    contentDescription = "Title",
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    /**
     * 徽标标题栏
     *
     * @param logo 徽标资源
     * @param title 标题资源
     * @param align 对齐方式
     * @param contentHeight 内容高度
     * @param fontWeight 字体粗细
     *
     * Created by Wu Qizhen on 2025.11.30
     * Updated by Wu Qizhen on 2026.1.15
     */
    @Composable
    fun LogoText(
        logo: Int,
        title: String,
        align: XAlignType = XAlignType.CENTER,
        contentHeight: Int = 25,
        fontWeight: FontWeight = FontWeight.Thin
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickVfx(),
            horizontalAlignment = if (XStats.isScreenRound()) Alignment.CenterHorizontally else when (align) {
                XAlignType.CENTER -> Alignment.CenterHorizontally
                XAlignType.START -> Alignment.Start
                XAlignType.END -> Alignment.End
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 25.dp,
                        bottom = XSpacings.getComponentSpacing()
                    )
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(contentHeight.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(XSpacings.getSpacing(spacingType = XSpacingType.COMPONENT_S)))

                Text(
                    text = title,
                    fontSize = (contentHeight - 5).sp,
                    fontFamily = XFont.THEME,
                    fontWeight = fontWeight,
                )
            }
        }
    }

    /**
     * 徽标标题栏
     *
     * @param logo 徽标资源
     * @param title 标题资源
     * @param align 对齐方式
     * @param contentHeight 内容高度
     * @param fontWeight 字体粗细
     * @param sharedTransitionScope 共享过渡作用域
     * @param animatedVisibilityScope 动画可见性作用域
     *
     * Created by Wu Qizhen on 2025.11.30
     * Updated by Wu Qizhen on 2026.1.15
     */
    @Composable
    fun LogoText(
        logo: Int,
        title: String,
        align: XAlignType = XAlignType.CENTER,
        contentHeight: Int = 25,
        fontWeight: FontWeight = FontWeight.Thin,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickVfx(),
            horizontalAlignment = if (XStats.isScreenRound()) Alignment.CenterHorizontally else when (align) {
                XAlignType.CENTER -> Alignment.CenterHorizontally
                XAlignType.START -> Alignment.Start
                XAlignType.END -> Alignment.End
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 25.dp,
                        bottom = XSpacings.getComponentSpacing()
                    )
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(id = logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "Logo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .size(contentHeight.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(XSpacings.getSpacing(spacingType = XSpacingType.COMPONENT_S)))

                    Text(
                        text = title,
                        fontSize = (contentHeight - 5).sp,
                        fontFamily = XFont.THEME,
                        fontWeight = fontWeight,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "Title"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        }
    }

    /**
     * 徽标标题栏
     *
     * @param logo 徽标资源
     * @param title 标题资源
     * @param align 对齐方式
     * @param contentHeight 内容高度
     * @param fontWeight 字体粗细
     *
     * Created by Wu Qizhen on 2025.11.30
     * Updated by Wu Qizhen on 2026.1.15
     */
    @Composable
    fun LogoText(
        logo: Int,
        title: Int,
        align: XAlignType = XAlignType.CENTER,
        contentHeight: Int = 25,
        fontWeight: FontWeight = FontWeight.Thin
    ) {
        LogoText(
            logo = logo,
            title = stringResource(id = title),
            align = align,
            contentHeight = contentHeight,
            fontWeight = fontWeight
        )
    }

    /**
     * 徽标标题栏
     *
     * @param logo 徽标资源
     * @param title 标题资源
     * @param align 对齐方式
     * @param contentHeight 内容高度
     * @param fontWeight 字体粗细
     * @param sharedTransitionScope 共享过渡作用域
     * @param animatedVisibilityScope 动画可见性作用域
     *
     * Created by Wu Qizhen on 2025.11.30
     * Updated by Wu Qizhen on 2026.1.15
     */
    @Composable
    fun LogoText(
        logo: Int,
        title: Int,
        align: XAlignType = XAlignType.CENTER,
        contentHeight: Int = 25,
        fontWeight: FontWeight = FontWeight.Thin,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        LogoText(
            logo = logo,
            title = stringResource(id = title),
            align = align,
            contentHeight = contentHeight,
            fontWeight = fontWeight,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }

    /**
     * ▼ 分类栏
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param planeColor 背景色
     * @param planeSize 背景大小
     * @param text 文本资源
     * @param textColor 文本颜色
     * @param fontSize 字体大小
     *
     * Created by Wu Qizhen on 2025.7.17
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Classification(
        icon: Int,
        iconColor: Color? = Color.White,
        iconSize: Int = 20,
        planeColor: Color? = Color.Transparent,
        planeSize: Int = iconSize,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME
    ) {
        Row(
            modifier = Modifier
                .clickVfx()
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*if (iconColor == null) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    modifier = Modifier.size(contentHeight.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    modifier = Modifier.size(contentHeight.dp),
                    tint = iconColor
                )
            }*/
            XIcon.RoundPlane(
                icon = icon,
                iconColor = iconColor,
                planeColor = planeColor,
                iconSize = iconSize,
                planeSize = planeSize
            )

            Spacer(modifier = Modifier.width(XSpacings.getSpacing(spacingType = XSpacingType.COMPONENT_S)))

            Text(
                text = text,
                color = textColor,
                fontSize = fontSize.sp,
                fontFamily = fontFamily,
                fontWeight = fontWeight
            )
        }
    }

    /**
     * ▼ 分类栏
     *
     * @param icon 图标资源
     * @param iconColor 图标颜色
     * @param iconSize 图标大小
     * @param planeColor 背景色
     * @param planeSize 背景大小
     * @param text 文本资源
     * @param textColor 文本颜色
     * @param fontSize 字体大小
     *
     * Created by Wu Qizhen on 2025.7.17
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Classification(
        icon: Int,
        iconColor: Color? = Color.White,
        iconSize: Int = 20,
        planeColor: Color? = Color.Transparent,
        planeSize: Int = iconSize,
        text: Int,
        textColor: Color = Color.White,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME
    ) {
        Classification(
            icon = icon,
            iconColor = iconColor,
            iconSize = iconSize,
            planeColor = planeColor,
            planeSize = planeSize,
            text = stringResource(id = text),
            textColor = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily
        )
    }

    /**
     * ▼ 分类栏
     *
     * @param icon 图标资源
     * @param text 文本资源
     * @param color 选项颜色
     * @param iconSize 图标大小
     * @param planeSize 背景大小
     * @param fontSize 字体大小
     *
     * Created by Wu Qizhen on 2026.1.15
     */
    @Composable
    fun Classification(
        icon: Int,
        text: String,
        color: XColorGroup = XColorGroup(
            border = Color.White,
            content = Color.White,
            background = Color.Transparent
        ),
        iconSize: Int = 20,
        planeSize: Int = iconSize,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        fontFamily: FontFamily = XFont.THEME
    ) {
        Classification(
            icon = icon,
            iconColor = color.content,
            iconSize = iconSize,
            planeColor = color.background,
            planeSize = planeSize,
            text = text,
            textColor = color.content ?: Color.White,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily
        )
    }
}