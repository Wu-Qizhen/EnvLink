package aethex.matrix.ui

import aethex.matrix.R
import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XBorderColor
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XColorsGroup
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.property.XWidth
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 卡片
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.11.29
 * Updated by Wu Qizhen on 2025.7.23
 * Updated by Wu Qizhen on 2026.1.16
 */
object XCard {
    /**
     * 颜色配置
     *
     * Created by Wu Qizhen on 2026.1.16
     */
    // val BACKGROUND_GRAY = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f)
    private val BACKGROUND_GRAY = Color.White.withAlpha(0.1f)
    private val BACKGROUND_GRAY_PRESSED = XBackgroundColor.DEFAULT_DARK_PRESSED.withAlpha(0.6f)
    private val BACKGROUND_THEME_ACTIVE = XBackgroundColor.THEME_LIGHT.withAlpha(0.2f)
    private val BORDER_GRAY = XBorderColor.GRAY
    private val BORDER_GRAY_GRADIENT = XBorderColor.GRAY_GRADIENT

    // val BORDER_GRAY_PRESSED = XBorderColor.GRAY
    private val BORDER_THEME_ACTIVE = XThemeColor.BASE

    /**
     * 平面卡片（无按压效果）
     *
     * Created by Wu Qizhen on 2024.11.29
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.11
     */
    /*@Composable
    fun Surface(
        padding: Int = 0,
        content: @Composable () -> Unit
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    color = XBackgroundColor.DEFAULT_DARK.withAlpha(0.8f),
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        colors = XBorderColor.GRAY_GRADIENT,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(padding.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }*/

    /**
     * 平面卡片（无按压效果）
     *
     * @param padding 卡片内间距
     * @param color 卡片背景色
     * @param borderRadius 卡片圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param modifier 卡片修饰符
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Surface(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorGroup/* = XColorGroup(
            background = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f),
            activeBackground = XBackgroundColor.DEFAULT_DARK_PRESSED
        )*/,
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        content: @Composable () -> Unit
    ) {
        Column(
            modifier = modifier
                // .wrapContentHeight()
                .background(
                    color = color.background ?: BACKGROUND_GRAY,
                    shape = RoundedCornerShape(borderRadius.dp)
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = color.border ?: XBorderColor.GRAY
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 平面卡片（无按压效果）
     *
     * @param padding 卡片内间距
     * @param color 卡片背景色
     * @param borderRadius 卡片圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param modifier 卡片修饰符
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Surface(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorsGroup = XColorsGroup(
            backgroundGradient = listOf(
                BACKGROUND_GRAY
            ),
            borderGradient = XBorderColor.GRAY_GRADIENT,
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        content: @Composable () -> Unit
    ) {
        Column(
            modifier = modifier
                // .wrapContentHeight()
                .background(
                    brush = Brush.linearGradient(
                        colors = color.backgroundGradient,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shape = RoundedCornerShape(borderRadius.dp)
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    brush = Brush.linearGradient(
                        colors = color.borderGradient,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 灵动卡片（有按压效果）
     *
     * // @param padding 卡片内间距
     * // @param content 卡片内容
     *
     * Created by Wu Qizhen on 2024.11.29
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Lively(
        padding: Int = 0,
        content: @Composable () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) XBackgroundColor.DEFAULT_DARK_PRESSED else XBackgroundColor.DEFAULT_DARK.withAlpha(
                0.3f
            )

        Column(
            modifier = Modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        colors = XBorderColor.GRAY_GRADIENT,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(padding.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }*/

    /**
     * 灵动卡片（有按压效果）
     *
     * // @param padding 卡片内间距
     * // @param modifier 卡片修饰符
     * // @param content 卡片内容
     *
     * Created by Wu Qizhen on 2025.9.6
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Lively(
        padding: Int = 0,
        modifier: Modifier,
        content: @Composable () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) XBackgroundColor.DEFAULT_DARK_PRESSED else XBackgroundColor.DEFAULT_DARK.withAlpha(
                0.3f
            )

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        colors = XBorderColor.GRAY_GRADIENT,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(padding.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }*/

    /**
     * 灵动卡片（有按压效果）
     *
     * @param padding 卡片内间距
     * @param color 卡片颜色
     * @param borderRadius 卡片圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param interactionSource 按压源
     * @param modifier 卡片修饰符
     * @param onClick 按压事件
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Lively(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorGroup/* = XColorGroup(
            background = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f),
            activeBackground = XBackgroundColor.DEFAULT_DARK_PRESSED
        )*/,
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) (color.activeBackground
                ?: BACKGROUND_GRAY_PRESSED) else (color.background ?: BACKGROUND_GRAY)
        val borderColor =
            if (isPressed.value) (color.activeBorder ?: Color.Transparent) else (color.border
                ?: Color.Transparent)

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = onClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = backgroundColor
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = borderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    @Composable
    fun Lively(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorGroup/* = XColorGroup(
            background = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f),
            activeBackground = XBackgroundColor.DEFAULT_DARK_PRESSED
        )*/,
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        content: @Composable () -> Unit
    ) {
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) (color.activeBackground
                ?: BACKGROUND_GRAY_PRESSED) else (color.background ?: BACKGROUND_GRAY)
        val borderColor =
            if (isPressed.value) (color.activeBorder ?: Color.Transparent) else (color.border
                ?: Color.Transparent)

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = backgroundColor
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = borderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 灵动卡片（有按压效果）
     *
     * @param padding 卡片内间距
     * @param color 卡片颜色
     * @param borderRadius 卡片圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param interactionSource 按压源
     * @param modifier 卡片修饰符
     * @param onClick 按压事件
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Lively(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorsGroup = XColorsGroup(
            backgroundGradient = listOf(BACKGROUND_GRAY),
            activeBackground = listOf(BACKGROUND_GRAY_PRESSED),
            borderGradient = BORDER_GRAY_GRADIENT
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color.activeBackground else color.backgroundGradient
        val borderColor =
            if (isPressed.value) color.activeBorder else color.borderGradient

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = onClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(borderRadius.dp),
                    brush = Brush.linearGradient(
                        colors = backgroundColor,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    brush = Brush.linearGradient(
                        colors = borderColor,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    @Composable
    fun Lively(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(0),
        color: XColorsGroup = XColorsGroup(
            backgroundGradient = listOf(BACKGROUND_GRAY),
            activeBackground = listOf(BACKGROUND_GRAY_PRESSED),
            borderGradient = BORDER_GRAY_GRADIENT
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        content: @Composable () -> Unit
    ) {
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color.activeBackground else color.backgroundGradient
        val borderColor =
            if (isPressed.value) color.activeBorder else color.borderGradient

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(borderRadius.dp),
                    brush = Brush.linearGradient(
                        colors = backgroundColor,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .border(
                    width = XWidth.BORDER_M,
                    shape = RoundedCornerShape(borderRadius.dp),
                    brush = Brush.linearGradient(
                        colors = borderColor,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 模块卡片
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Block(
        padding: XPadding = XPadding.all(10),
        cardColor: Color = BACKGROUND_GRAY,
        borderColor: Color? = null,
        borderRadius: Int = 15,
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 20,
        planeColor: Color? = null,
        planeSize: Int = 30,
        title: String,
        titleColor: Color? = null,
        titleSize: Int = 16,
        subTitle: @Composable () -> Unit = {},
        iconTextPadding: XPadding = XPadding.all(5),
        headerContentPadding: XPadding = XPadding.all(padding.top),
        showArrow: Boolean = false,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        Lively(
            padding = padding,
            color = XColorGroup(
                background = cardColor,
                border = borderColor ?: Color.Transparent,
            ),
            borderRadius = borderRadius,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconColor,
                    iconSize = iconSize,
                    planeColor = planeColor,
                    planeSize = planeSize
                )

                /*if (planeColor != null) {
                    XIcon.RoundPlane(
                        icon,
                        iconColor ?: Color.White,
                        iconSize,
                        planeColor,
                        planeSize
                    )
                } else if (iconColor == null) {
                    Icon(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(id = icon),
                        tint = iconColor,
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )
                }*/

                Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                Text(
                    text = title,
                    fontSize = titleSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor ?: Color.White
                )

                if (showArrow) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.width(iconTextPadding.start.dp))
                        subTitle()
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = titleColor ?: Color.White,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(headerContentPadding.top.dp))

            content()
        }
    }

    /**
     * 模块卡片
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Block(
        padding: XPadding = XPadding.all(10),
        cardColor: List<Color>,
        borderColor: List<Color>? = null,
        borderRadius: Int = 15,
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 20,
        planeColor: Color? = null,
        planeSize: Int = 30,
        title: String,
        titleColor: Color? = null,
        titleSize: Int = 16,
        iconTextPadding: XPadding = XPadding.all(5),
        headerContentPadding: XPadding = XPadding.all(padding.top),
        showArrow: Boolean = false,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        Lively(
            padding = padding,
            color = XColorsGroup(
                backgroundGradient = cardColor,
                borderGradient = borderColor ?: listOf(Color.Transparent),
            ),
            borderRadius = borderRadius,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconColor,
                    iconSize = iconSize,
                    planeColor = planeColor,
                    planeSize = planeSize
                )

                /*if (planeColor != null) {
                    XIcon.RoundPlane(
                        icon,
                        iconColor ?: Color.White,
                        iconSize,
                        planeColor,
                        planeSize
                    )
                } else if (iconColor == null) {
                    Icon(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(id = icon),
                        tint = iconColor,
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )
                }*/

                Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                Text(
                    text = title,
                    fontSize = titleSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor ?: Color.White
                )

                if (showArrow) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.width(iconTextPadding.end.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = titleColor ?: Color.White,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(headerContentPadding.top.dp))

            content()
        }
    }

    /**
     * 模块卡片
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Block(
        padding: XPadding = XPadding.all(10),
        cardColor: Color = BACKGROUND_GRAY,
        borderColor: Color? = null,
        borderRadius: Int = 15,
        icon: Int,
        iconColor: XColorGroup = XColorGroup(
            background = null,
            content = null
        ),
        iconSize: Int = 20,
        planeSize: Int = 30,
        title: String,
        titleColor: Color? = null,
        titleSize: Int = 16,
        subTitle: @Composable () -> Unit = {},
        iconTextPadding: XPadding = XPadding.all(5),
        headerContentPadding: XPadding = XPadding.all(padding.top),
        showArrow: Boolean = false,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        /*Lively(
            padding = padding,
            color = XColorGroup(
                background = cardColor,
                border = borderColor ?: Color.Transparent,
            ),
            borderRadius = borderRadius,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconSize = iconSize,
                    color = iconColor,
                    planeSize = planeSize
                )

                Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                Text(
                    text = title,
                    fontSize = titleSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor ?: Color.White
                )
            }

            Spacer(modifier = Modifier.height(padding.top.dp))

            content()
        }*/

        Block(
            padding = padding,
            cardColor = cardColor,
            borderColor = borderColor,
            borderRadius = borderRadius,
            icon = icon,
            iconColor = iconColor.content,
            iconSize = iconSize,
            planeColor = iconColor.background,
            planeSize = planeSize,
            title = title,
            titleColor = titleColor,
            titleSize = titleSize,
            subTitle = subTitle,
            iconTextPadding = iconTextPadding,
            headerContentPadding = headerContentPadding,
            showArrow = showArrow,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            onLongClick = onLongClick,
            content = content
        )
    }

    /**
     * 模块卡片
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Block(
        padding: XPadding = XPadding.all(10),
        cardColor: List<Color>,
        borderColor: List<Color>? = null,
        borderRadius: Int = 15,
        icon: Int,
        iconColor: XColorGroup = XColorGroup(
            background = null,
            content = null
        ),
        iconSize: Int = 20,
        planeSize: Int = 30,
        title: String,
        titleColor: Color? = null,
        titleSize: Int = 16,
        iconTextPadding: XPadding = XPadding.all(5),
        headerContentPadding: XPadding = XPadding.all(padding.top),
        showArrow: Boolean = false,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        /*Lively(
            padding = padding,
            color = XColorsGroup(
                backgroundGradient = cardColor,
                borderGradient = borderColor ?: listOf(Color.Transparent),
            ),
            borderRadius = borderRadius,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconSize = iconSize,
                    color = iconColor,
                    planeSize = planeSize
                )

                Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                Text(
                    text = title,
                    fontSize = titleSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor ?: Color.White
                )
            }

            Spacer(modifier = Modifier.height(padding.top.dp))

            content()
        }*/

        Block(
            padding = padding,
            cardColor = cardColor,
            borderColor = borderColor,
            borderRadius = borderRadius,
            icon = icon,
            iconColor = iconColor.content,
            iconSize = iconSize,
            planeColor = iconColor.background,
            planeSize = planeSize,
            title = title,
            titleColor = titleColor,
            titleSize = titleSize,
            iconTextPadding = iconTextPadding,
            headerContentPadding = headerContentPadding,
            showArrow = showArrow,
            horizontalAlignment = horizontalAlignment,
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            content = content
        )
    }

    /**
     * 开关卡片
     *
     * @param padding 卡片内间距
     * @param borderRadius 卡片边框圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param interactionSource 按压源
     * @param modifier 卡片修饰符
     * @param status 开关状态
     * @param onClick 按压事件
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Switch(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(15),
        color: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        status: MutableState<Boolean>,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        val dynamicBackgroundColor by animateColorAsState(
            targetValue = if (status.value) (color.activeBackground
                ?: BACKGROUND_THEME_ACTIVE) else (color.background ?: BACKGROUND_GRAY),
        )
        val dynamicBorderColor by animateColorAsState(
            targetValue = if (status.value) (color.activeBorder
                ?: BORDER_THEME_ACTIVE) else (color.border ?: BORDER_GRAY)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status.value) 2.0f.dp else XWidth.BORDER_M
        )

        Column(
            modifier = modifier
                .clickVfx(interactionSource, true, onClick)
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(dynamicBackgroundColor, RoundedCornerShape(borderRadius.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = dynamicBorderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    @Composable
    fun Switch(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(15),
        color: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        status: MutableState<Boolean>,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        content: @Composable () -> Unit
    ) {
        val dynamicBackgroundColor by animateColorAsState(
            targetValue = if (status.value) (color.activeBackground
                ?: BACKGROUND_THEME_ACTIVE) else (color.background ?: BACKGROUND_GRAY),
        )
        val dynamicBorderColor by animateColorAsState(
            targetValue = if (status.value) (color.activeBorder
                ?: BORDER_THEME_ACTIVE) else (color.border ?: BORDER_GRAY)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status.value) 2.0f.dp else XWidth.BORDER_M
        )

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource,
                    true,
                    onClick,
                    onLongClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(dynamicBackgroundColor, RoundedCornerShape(borderRadius.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = dynamicBorderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 开关卡片
     *
     * @param padding 卡片内间距
     * @param borderRadius 卡片边框圆角
     * @param horizontalAlignment 卡片水平对齐方式
     * @param interactionSource 按压源
     * @param modifier 卡片修饰符
     * @param status 开关状态
     * @param onClick 按压事件
     * @param content 卡片内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Switch(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(15),
        color: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        status: Boolean,
        onClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        val dynamicBackgroundColor by animateColorAsState(
            targetValue = if (status) (color.activeBackground
                ?: BACKGROUND_THEME_ACTIVE) else (color.background ?: BACKGROUND_GRAY),
        )
        val dynamicBorderColor by animateColorAsState(
            targetValue = if (status) (color.activeBorder
                ?: BORDER_THEME_ACTIVE) else (color.border ?: BORDER_GRAY)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status) 2.0f.dp else XWidth.BORDER_M
        )

        Column(
            modifier = modifier
                .clickVfx(interactionSource, true, onClick)
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(dynamicBackgroundColor, RoundedCornerShape(borderRadius.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = dynamicBorderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    @Composable
    fun Switch(
        modifier: Modifier = Modifier,
        padding: XPadding = XPadding.all(15),
        color: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        borderRadius: Int = 15,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        status: Boolean,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        content: @Composable () -> Unit
    ) {
        val dynamicBackgroundColor by animateColorAsState(
            targetValue = if (status) (color.activeBackground
                ?: BACKGROUND_THEME_ACTIVE) else (color.background ?: BACKGROUND_GRAY),
        )
        val dynamicBorderColor by animateColorAsState(
            targetValue = if (status) (color.activeBorder
                ?: BORDER_THEME_ACTIVE) else (color.border ?: BORDER_GRAY)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status) 2.0f.dp else XWidth.BORDER_M
        )

        Column(
            modifier = modifier
                .clickVfx(
                    interactionSource,
                    true,
                    onClick,
                    onLongClick
                )
                // .wrapContentHeight()
                // .fillMaxWidth()
                .background(dynamicBackgroundColor, RoundedCornerShape(borderRadius.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(borderRadius.dp),
                    color = dynamicBorderColor
                )
                .padding(
                    start = padding.start.dp,
                    top = padding.top.dp,
                    end = padding.end.dp,
                    bottom = padding.bottom.dp
                ),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }
}