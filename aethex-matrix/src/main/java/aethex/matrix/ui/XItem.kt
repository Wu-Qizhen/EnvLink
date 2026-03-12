package aethex.matrix.ui

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XBorderColor
import aethex.matrix.foundation.color.XButtonColor
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.XContentColor
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.property.XWidth
import aethex.matrix.foundation.type.XButtonType
import aethex.matrix.foundation.type.XColorType
import aethex.matrix.foundation.typography.XFont
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 胶囊按钮 -> 项目按钮
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.6.16
 * Updated by Wu Qizhen on 2024.8.31
 * Updated by Wu Qizhen on 2024.11.30
 * Updated by Wu Qizhen on 2024.12.31
 * Updated by Wu Qizhen on 2025.7.23
 * Updated by Wu Qizhen on 2026.1.13
 * Updated by Wu Qizhen on 2026.1.16
 */
object XItem {
    /**
     * 颜色配置
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    // private val BACKGROUND_GRAY = XBackgroundColor.DEFAULT_DARK.withAlpha(0.4f)
    private val BACKGROUND_GRAY = Color.White.withAlpha(0.2f)

    // private val BACKGROUND_GRAY_PRESSED = XBackgroundColor.DEFAULT_DARK_PRESSED.withAlpha(0.6f)
    private val BACKGROUND_GRAY_PRESSED = XBackgroundColor.DEFAULT_DARK_PRESSED.withAlpha(0.6f)
    private val BORDER_GRAY = XBorderColor.GRAY
    private val BORDER_GRAY_GRADIENT = XBorderColor.GRAY_GRADIENT
    private val BORDER_WIDTH = XWidth.BORDER_M
    private val BACKGROUND_THEME = XThemeColor.BASE
    private val BACKGROUND_THEME_PRESSED = XThemeColor.DARK
    private val CONTENT_THEME = XThemeColor.BASE
    private val CONTENT_GRAY = XContentColor.GRAY_DARK
    private val PLANE_GRAY = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f)
    private val BACKGROUND_THEME_ACTIVE = XBackgroundColor.THEME_LIGHT.withAlpha(0.2f)
    private val BORDER_THEME_ACTIVE = XThemeColor.BASE
    // private val BACKGROUND_THEME_SWITCH = XThemeColor.DARKER

    /**
     * 文本按钮
     *
     * @param text 按钮文字
     * @param enabled 是否可用
     * @param color 按钮颜色
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Button(
        modifier: Modifier = Modifier,
        text: String,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        enabled: Boolean = true,
        color: XColorGroup = XButtonColor.THEME_LIGHT,
        padding: XPadding = XPadding.vertical(10).horizontal(20),
        borderRadius: Int = 50,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (enabled) {
                if (isPressed.value) (color.activeBackground
                    ?: BACKGROUND_THEME_PRESSED) else (color.background ?: BACKGROUND_THEME)
            } else {
                Color.DarkGray
            }

        Box(
            modifier = modifier
                .clickVfx(interactionSource, enabled, onClick)
                .background(backgroundColor, RoundedCornerShape(borderRadius.dp))
                .padding(
                    start = padding.start.dp,
                    end = padding.end.dp,
                    top = padding.top.dp,
                    bottom = padding.bottom.dp
                )
        ) {
            Text(
                modifier = Modifier
                    .basicMarquee()
                    .align(Alignment.Center),
                text = text,
                fontSize = fontSize.sp,
                maxLines = 1,
                fontWeight = fontWeight,
                color = if (enabled) (color.content ?: Color.White) else Color.LightGray
            )
        }
    }

    /**
     * 文本按钮
     *
     * @param text 按钮文字
     * @param enabled 是否可用
     * @param buttonType 按钮类型
     * @param colorType 按钮颜色类型
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Button(
        modifier: Modifier = Modifier,
        text: String,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        enabled: Boolean = true,
        buttonType: XButtonType = XButtonType.THEME,
        colorType: XColorType = XColorType.LIGHT,
        padding: XPadding = XPadding.vertical(10).horizontal(20),
        borderRadius: Int = 50,
        onClick: () -> Unit
    ) {
        val color = XButtonColor.getColor(buttonType, colorType)

        this.Button(
            modifier = modifier,
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            enabled = enabled,
            color = color,
            padding = padding,
            borderRadius = borderRadius,
            onClick = onClick
        )
    }

    /**
     * 文本按钮
     *
     * Created by Wu Qizhen on 2025.9.6
     * Deprecated on 2025.11.30
     */
    /*@Composable
    fun Button(
        text: String,
        enabled: Boolean = true,
        modifier: Modifier,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (enabled) {
                if (isPressed.value) BACKGROUND_THEME_PRESSED else BACKGROUND_THEME
            } else {
                Color.DarkGray
            }

        Box(
            modifier = modifier
                .clickVfx(interactionSource, enabled, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Color.White else Color.LightGray
            )
        }
    }*/

    /**
     * 文本按钮
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     *  Deprecated on 2025.11.30
     */
    /*@Composable
    fun Button(
        text: String,
        color: List<Color>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color[1] else color[0]

        Box(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color[2]
            )
        }
    }*/

    /**
     * 图标按钮
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     *  Deprecated on 2025.11.30
     */
    /*@Composable
    fun Button(
        icon: Int,
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_THEME_PRESSED else BACKGROUND_THEME

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = Color.White,
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
                // color = ContentColor.DEFAULT_BROWN
            )
        }
    }*/

    /**
     * 图标按钮
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2025.11.30
     */
    /*@Composable
    fun Button(
        icon: Int,
        text: String,
        color: List<Color>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color[1] else color[0]

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = color[2],
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color[2]
            )
        }
    }*/

    /**
     * 图标按钮
     *
     * @param icon 图标
     * @param text 按钮文字
     * @param enabled 是否可用
     * @param color 颜色
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Button(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        iconTextPadding: XPadding = XPadding.all(5),
        text: String,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        enabled: Boolean = true,
        color: XColorGroup = XButtonColor.THEME_LIGHT,
        padding: XPadding = XPadding.vertical(10).horizontal(20),
        borderRadius: Int = 50,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (enabled) {
                if (isPressed.value) (color.activeBackground
                    ?: BACKGROUND_THEME_PRESSED) else (color.background ?: BACKGROUND_THEME)
            } else {
                Color.DarkGray
            }

        Row(
            modifier = modifier
                .clickVfx(interactionSource, enabled, onClick)
                .background(backgroundColor, RoundedCornerShape(borderRadius.dp))
                .padding(
                    top = padding.top.dp,
                    bottom = padding.bottom.dp,
                    start = padding.start.dp,
                    end = padding.end.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = if (enabled) (color.content ?: Color.White) else Color.LightGray,
                modifier = Modifier.size(iconSize.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                color = if (enabled) (color.content ?: Color.White) else Color.LightGray
            )
        }
    }

    /**
     * 图标按钮
     *
     * @param icon 图标
     * @param text 按钮文字
     * @param enabled 是否可用
     * @param buttonType 按钮类型
     * @param colorType 按钮颜色类型
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Button(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        iconTextPadding: XPadding = XPadding.all(5),
        text: String,
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        enabled: Boolean = true,
        buttonType: XButtonType = XButtonType.THEME,
        colorType: XColorType = XColorType.LIGHT,
        padding: XPadding = XPadding.vertical(10).horizontal(20),
        borderRadius: Int = 50,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
        onClick: () -> Unit
    ) {
        val color = XButtonColor.getColor(buttonType, colorType)

        this.Button(
            modifier = modifier,
            icon = icon,
            iconSize = iconSize,
            iconTextPadding = iconTextPadding,
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            enabled = enabled,
            color = color,
            padding = padding,
            borderRadius = borderRadius,
            horizontalArrangement = horizontalArrangement,
            onClick = onClick
        )
    }

    /**
     * 双按钮
     *
     * @param iconOne 图标 1
     * @param iconTwo 图标 2
     * @param textOne 文字 1
     * @param textTwo 文字 2
     * @param enabledOne 是否可用 1
     * @param enabledTwo 是否可用 2
     * @param colorOne 颜色 1
     * @param colorTwo 颜色 2
     * @param onClickOne 点击事件 1
     * @param onClickTwo 点击事件 2
     * @param enabledCompact 是否启用紧凑模式
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Updated by Wu Qizhen on 2025.11.30
     */
    @Composable
    fun Bibutton(
        modifier: Modifier = Modifier,
        iconOne: Int,
        iconTwo: Int,
        textOne: String,
        textTwo: String,
        enabledOne: Boolean = true,
        enabledTwo: Boolean = true,
        colorOne: XColorGroup = XButtonColor.THEME_LIGHT,
        colorTwo: XColorGroup = XButtonColor.THEME_LIGHT,
        onClickOne: () -> Unit,
        onClickTwo: () -> Unit,
        iconSize: Int = 20,
        iconTextPadding: XPadding = XPadding.all(5),
        fontSize: Int = 16,
        fontWeight: FontWeight = FontWeight.Bold,
        padding: XPadding = XPadding.vertical(10).start(15).end(10),
        betweenPadding: XPadding = XPadding.all(5),
        borderRadius: XPadding = XPadding.only(start = 50, end = 5),
        enabledCompact: Boolean = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (!enabledCompact) {
                Button(
                    modifier = modifier,
                    icon = iconOne,
                    iconSize = iconSize,
                    iconTextPadding = iconTextPadding,
                    text = textOne,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    enabled = enabledOne,
                    color = colorOne,
                    padding = padding,
                    borderRadius = borderRadius.start,
                    horizontalArrangement = Arrangement.Center,
                    onClick = onClickOne
                )

                Spacer(modifier = Modifier.width(betweenPadding.start.dp))

                Button(
                    modifier = modifier,
                    icon = iconTwo,
                    iconSize = iconSize,
                    iconTextPadding = iconTextPadding,
                    text = textTwo,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    enabled = enabledTwo,
                    color = colorTwo,
                    padding = padding,
                    borderRadius = borderRadius.start,
                    horizontalArrangement = Arrangement.Center,
                    onClick = onClickTwo
                )
            } else {
                val interactionSourceLeft = remember { MutableInteractionSource() }
                val isPressedLeft = interactionSourceLeft.collectIsPressedAsState()
                val interactionSourceRight = remember { MutableInteractionSource() }
                val isPressedRight = interactionSourceRight.collectIsPressedAsState()
                val backgroundColorLeft =
                    if (enabledOne) {
                        if (isPressedLeft.value) (colorOne.activeBackground
                            ?: BACKGROUND_THEME_PRESSED) else (colorOne.background
                            ?: BACKGROUND_THEME)
                    } else {
                        Color.DarkGray
                    }
                val backgroundColorRight =
                    if (enabledTwo) {
                        if (isPressedRight.value) (colorTwo.activeBackground
                            ?: BACKGROUND_THEME_PRESSED) else (colorTwo.background
                            ?: BACKGROUND_THEME)
                    } else {
                        Color.DarkGray
                    }

                Row(
                    modifier = modifier
                        .clickVfx(interactionSourceLeft, enabledOne, onClickOne)
                        .background(
                            backgroundColorLeft,
                            RoundedCornerShape(
                                topStart = borderRadius.start.dp,
                                bottomStart = borderRadius.start.dp,
                                topEnd = borderRadius.end.dp,
                                bottomEnd = borderRadius.end.dp
                            )
                        )
                        .padding(
                            top = padding.top.dp,
                            bottom = padding.bottom.dp,
                            start = padding.start.dp,
                            end = padding.end.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconOne),
                        tint = if (enabledOne) (colorOne.content
                            ?: Color.White) else Color.LightGray,
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                    Text(
                        text = textOne,
                        fontSize = fontSize.sp,
                        fontWeight = fontWeight,
                        color = if (enabledOne) (colorOne.content
                            ?: Color.White) else Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.width(betweenPadding.start.dp))

                Row(
                    modifier = modifier
                        .clickVfx(interactionSourceRight, enabledTwo, onClickTwo)
                        .background(
                            backgroundColorRight, RoundedCornerShape(
                                topStart = borderRadius.end.dp,
                                bottomStart = borderRadius.end.dp,
                                topEnd = borderRadius.start.dp,
                                bottomEnd = borderRadius.start.dp
                            )
                        )
                        .padding(
                            top = padding.top.dp,
                            bottom = padding.bottom.dp,
                            start = padding.end.dp,
                            end = padding.start.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconTwo),
                        tint = if (enabledTwo) (colorTwo.content
                            ?: Color.White) else Color.LightGray,
                        modifier = Modifier.size(iconSize.dp),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.size(iconTextPadding.start.dp))

                    Text(
                        text = textTwo,
                        fontSize = fontSize.sp,
                        fontWeight = fontWeight,
                        color = if (enabledTwo) (colorTwo.content
                            ?: Color.White) else Color.LightGray
                    )
                }
            }
        }
    }

    /**
     * 胶囊按钮
     *
     * // @param image 图标
     * // @param text 按钮文字
     * // @param subText 副标题
     * // @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        image: Int,
        text: String,
        subText: String? = null,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (subText != null) {
                    Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )

                    Text(
                        text = subText,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }*/

    /**
     * 胶囊按钮
     *
     * // @param image 图标
     * // @param text 按钮文字
     * // @param subText 副标题
     * // @param onClick 点击事件
     * // @param onLongClick 长按事件
     *
     * Created by Wu Qizhen on 2025.11.30
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        image: Int,
        text: String,
        subText: String? = null,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick, onLongClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (subText != null) {
                    Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )

                    Text(
                        text = subText,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }*/

    /**
     * 胶囊按钮
     *
     * // @param icon 图标
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param subText 副标题
     * // @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }*/

    /**
     * 胶囊按钮
     *
     * // @param icon 图标
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param subText 副标题
     * // @param onClick 点击事件
     * // @param onLongClick 长按事件
     *
     * Created by Wu Qizhen on 2025.11.30
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick, onLongClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }*/

    /**
     * 胶囊按钮
     *
     * // @param icon 图标
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1
            )
        }
    }*/

    /**
     * 胶囊按钮
     *
     * // @param icon 图标
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param onClick 点击事件
     * // @param onLongClick 长按事件
     *
     * Created by Wu Qizhen on 2025.11.30
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick, onLongClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1
            )
        }
    }*/

    /**
     * 卡片按钮
     *
     * @param icon 图标
     * @param text 按钮文字
     * @param cardSize 卡片大小
     * @param iconSize 图标大小
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Card(
        modifier: Modifier = Modifier,
        icon: Int,
        text: String,
        iconSize: Int = 30,
        aspectRatio: Float = 1f,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Box(
            modifier = modifier
                .clickVfx(interactionSource, true, onClick)
                .aspectRatio(aspectRatio)
                .background(backgroundColor, RoundedCornerShape(15.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY_GRADIENT,
                        start = Offset.Infinite,
                        end = Offset.Zero
                    )
                )
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                modifier = Modifier.size(iconSize.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
                    .align(Alignment.BottomStart)
            )
        }
    }

    /**
     * 卡片按钮
     *
     * // @param icon 图标
     * // @param text 按钮文字
     * // @param cardSize 卡片大小
     * // @param iconSize 图标大小
     * // @param onClick 点击事件
     * // @param onLongClick 长按事件
     *
     * Created by Wu Qizhen on 2025.11.30
     * Deprecated on 2026.1.16
     */
    /*@Composable
    fun Card(
        icon: Int,
        text: String,
        cardSize: Int = 85,
        iconSize: Int = 30,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_GRAY_PRESSED else BACKGROUND_GRAY

        Box(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick, onLongClick)
                .size(cardSize.dp)
                .background(backgroundColor, RoundedCornerShape(15.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        BORDER_GRAY,
                        start = Offset.Infinite,
                        end = Offset.Zero
                    )
                )
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                modifier = Modifier.size(iconSize.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.BottomStart)
            )
        }
    }*/

    /**
     * 切换按钮
     *
     * // @param icon 图标
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param subText 副文本
     * // @param status 状态
     * // @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2024.8.31
     * Updated by Wu Qizhen on 2024.11.30
     * Updated by Wu Qizhen on 2024.12.31
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Switch(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        color: XColorGroup = XButtonColor.THEME_DARK,
        status: State<Boolean>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        // val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (status.value) color.background.withAlpha(0.8f) else BACKGROUND_GRAY
        val borderColor by animateColorAsState(
            targetValue = if (status.value) color.content else Color(
                23,
                23,
                23,
                255
            ), label = ""
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status.value) 2.0f.dp else BORDER_WIDTH,
            label = ""
        )

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(50.dp),
                    color = borderColor
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }*/

    /**
     * 切换按钮
     *
     * // @param icon 图标
     * // @param iconColor 图标颜色
     * // @param iconSize 图标大小
     * // @param text 按钮文字
     * // @param textColor 文字颜色
     * // @param subText 副文本
     * // @param subTextColor 副文本颜色
     * // @param padding 内边距
     * // @param backgroundColor 背景颜色
     * // @param borderRadius 边框圆角
     * // @param borderColor 边框颜色
     * // @param activeBorderColor 激活边框颜色
     * // @param interactionSource 交互源
     * // @param modifier 修饰符
     * // @param status 状态
     *
     * Created by Wu Qizhen on 2026.1.11
     * Deprecated on 2026.1.13
     */
    /*@Composable
    fun Switch(
        icon: Int,
        iconColor: Color? = null,
        iconSize: Int = 30,
        wrapIcon: Boolean = false,
        text: String,
        textColor: Color = Color.White,
        subText: String,
        subTextColor: Color = Color.Gray,
        padding: Int = 10,
        backgroundColor: Color = XBackgroundColor.DEFAULT_DARK.withAlpha(0.3f),
        activeBackgroundColor: Color = XBackgroundColor.DEFAULT_LIGHT.withAlpha(0.2f),
        borderRadius: Int = 50,
        borderColor: Color = XBorderColor.GRAY,
        activeBorderColor: Color = XBackgroundColor.DEFAULT_LIGHT,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        modifier: Modifier = Modifier.fillMaxWidth(),
        status: State<Boolean>,
        onClick: () -> Unit
    ) {
        XCard.Switch(
            padding = XPadding.all(padding),
            color = XColorGroup(
                background = backgroundColor,
                activeBackground = activeBackgroundColor,
                border = borderColor,
                activeBorder = activeBorderColor,
                content = textColor,
                activeContent = textColor
            ),
            borderRadius = borderRadius,
            interactionSource = interactionSource,
            modifier = modifier,
            status = status,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconColor != null) {
                    if (wrapIcon) {
                        Box(
                            modifier = Modifier
                                .size(iconSize.dp)
                                .background(
                                    XBackgroundColor.DEFAULT_LIGHT.withAlpha(0.3f),
                                    RoundedCornerShape(iconSize.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                modifier = Modifier
                                    .size((iconSize * 0.6).dp)
                                    .align(Alignment.Center),
                                contentDescription = null,
                                tint = iconColor
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = icon),
                            modifier = Modifier
                                .size(iconSize.dp),
                            // .padding(iconPadding)
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = icon),
                        modifier = Modifier
                            .size(30.dp),
                        // .padding(iconPadding)
                        contentDescription = null
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 14.sp,
                        maxLines = 1
                    )

                    Text(
                        text = subText,
                        fontSize = 12.sp,
                        color = subTextColor,
                        maxLines = 1
                    )
                }
            }
        }
    }*/

    /**
     * 块按钮
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {}
    ) {
        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            borderRadius = borderRadius
        ) {
            XIcon.RoundPlane(
                icon = icon,
                iconColor = iconPlaneColor.content,
                iconSize = iconSize,
                planeColor = iconPlaneColor.background,
                planeSize = planeSize
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )
            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            onLongClick = onLongClick,
            borderRadius = borderRadius
        ) {
            XIcon.RoundPlane(
                icon = icon,
                iconColor = iconPlaneColor.content,
                iconSize = iconSize,
                planeColor = iconPlaneColor.background,
                planeSize = planeSize
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )
            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: Int,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {}
    ) {
        Block(
            modifier = modifier,
            icon = icon,
            iconSize = iconSize,
            planeSize = planeSize,
            text = stringResource(id = text),
            textColor = textColor,
            fontSize = fontSize,
            subText = subText,
            subTextColor = subTextColor,
            subFontSize = subFontSize,
            padding = padding,
            itemColor = itemColor,
            iconPlaneColor = iconPlaneColor,
            borderRadius = borderRadius,
            onClick = onClick
        )
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: Int,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        Block(
            modifier = modifier,
            icon = icon,
            iconSize = iconSize,
            planeSize = planeSize,
            text = stringResource(id = text),
            textColor = textColor,
            fontSize = fontSize,
            subText = subText,
            subTextColor = subTextColor,
            subFontSize = subFontSize,
            padding = padding,
            itemColor = itemColor,
            iconPlaneColor = iconPlaneColor,
            borderRadius = borderRadius,
            onClick = onClick,
            onLongClick = onLongClick
        )
    }

    /**
     * 块按钮
     *
     * Created by Wu Qizhen on 2026.1.13
     * Recommend
     */
    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {}
    ) {
        val dynamicPlaneColor by animateColorAsState(
            targetValue = if (status.value) (iconPlaneColor.activeBackground
                ?: Color.White) else (iconPlaneColor.background ?: PLANE_GRAY)
        )
        val dynamicIconColor by animateColorAsState(
            targetValue = if (status.value) (iconPlaneColor.activeContent
                ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White)
        )

        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            borderRadius = borderRadius
        ) {
            /*if (status.value) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.activeContent,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.activeBackground,
                    planeSize = planeSize
                )
            } else {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.background,
                    planeSize = planeSize
                )
            }*/

            XIcon.RoundPlane(
                icon = icon,
                iconColor = dynamicIconColor,
                iconSize = iconSize,
                planeColor = dynamicPlaneColor,
                planeSize = planeSize
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )

            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        val dynamicPlaneColor by animateColorAsState(
            targetValue = if (status.value) (iconPlaneColor.activeBackground
                ?: Color.White) else (iconPlaneColor.background ?: PLANE_GRAY)
        )
        val dynamicIconColor by animateColorAsState(
            targetValue = if (status.value) (iconPlaneColor.activeContent
                ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White)
        )

        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            onLongClick = onLongClick,
            borderRadius = borderRadius
        ) {
            XIcon.RoundPlane(
                icon = icon,
                iconColor = dynamicIconColor,
                iconSize = iconSize,
                planeColor = dynamicPlaneColor,
                planeSize = planeSize
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )

            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        status: Boolean,
        onClick: () -> Unit = {}
    ) {
        val dynamicPlaneColor by animateColorAsState(
            targetValue = if (status) (iconPlaneColor.activeBackground
                ?: Color.White) else (iconPlaneColor.background ?: PLANE_GRAY)
        )
        val dynamicIconColor by animateColorAsState(
            targetValue = if (status) (iconPlaneColor.activeContent
                ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White)
        )

        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            borderRadius = borderRadius
        ) {
            /*if (status.value) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.activeContent,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.activeBackground,
                    planeSize = planeSize
                )
            } else {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.background,
                    planeSize = planeSize
                )
            }*/

            XIcon.RoundPlane(
                icon = icon,
                iconColor = dynamicIconColor,
                iconSize = iconSize,
                planeColor = dynamicPlaneColor,
                planeSize = planeSize
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )

            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    fun Block(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        status: Boolean,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        val dynamicPlaneColor by animateColorAsState(
            targetValue = if (status) (iconPlaneColor.activeBackground
                ?: Color.White) else (iconPlaneColor.background ?: PLANE_GRAY)
        )
        val dynamicIconColor by animateColorAsState(
            targetValue = if (status) (iconPlaneColor.activeContent
                ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White)
        )

        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            onClick = onClick,
            onLongClick = onLongClick,
            borderRadius = borderRadius
        ) {
            XIcon.RoundPlane(
                icon = icon,
                iconColor = dynamicIconColor,
                iconSize = iconSize,
                planeColor = dynamicPlaneColor,
                planeSize = planeSize
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.basicMarquee(),
                overflow = TextOverflow.Ellipsis
            )

            if (subText != null) {
                Text(
                    text = subText,
                    fontSize = subFontSize.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    /**
     * 胶囊按钮（无状态）
     *
     * Created by Wu Qizhen on 2026.1.13
     */
    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White,
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {}
    ) {
        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.background ?: PLANE_GRAY,
                    planeSize = planeSize
                )
                Column {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_GRAY_PRESSED,
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            content = Color.White,
        ),
        borderRadius: Int = 15,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        XCard.Lively(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = iconPlaneColor.background ?: PLANE_GRAY,
                    planeSize = planeSize
                )
                Column {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    /**
     * 胶囊按钮
     *
     * Created by Wu Qizhen on 2026.1.13
     * Recommend
     */
    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = if (status.value) iconPlaneColor.activeContent else iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = if (status.value) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status.value,
                        onCheckedChange = {
                            status.value = it
                        },
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = if (status.value) iconPlaneColor.activeContent else iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = if (status.value) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status.value,
                        onCheckedChange = {
                            status.value = it
                        },
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        iconTextPadding: XPadding = XPadding.horizontal(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        showSwitcher: Boolean = false,
        status: Boolean,
        onClick: () -> Unit = {},
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = verticalAlignment,
                horizontalArrangement = horizontalArrangement
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = if (status) iconPlaneColor.activeContent else iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = if (status) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Spacer(modifier = Modifier.width(iconTextPadding.start.dp))
                Column(
                    modifier = if (showSwitcher) Modifier.weight(1f) else Modifier
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        icon: Int,
        iconSize: Int = 20,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: Boolean,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = if (status) iconPlaneColor.activeContent else iconPlaneColor.content,
                    iconSize = iconSize,
                    planeColor = if (status) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    /**
     * 胶囊按钮（字体）
     *
     * Created by Wu Qizhen on 2026.1.13
     * Recommend
     */
    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        iconText: String,
        iconFontSize: Int = 16,
        iconFontWeight: FontWeight = FontWeight.Bold,
        iconFontFamily: FontFamily = XFont.THEME,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    text = iconText,
                    textColor = if (status.value) (iconPlaneColor.activeContent
                        ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White),
                    fontSize = iconFontSize,
                    fontWeight = iconFontWeight,
                    fontFamily = iconFontFamily,
                    planeColor = if (status.value) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status.value,
                        onCheckedChange = {
                            status.value = it
                        },
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        iconText: String,
        iconFontSize: Int = 16,
        iconFontWeight: FontWeight = FontWeight.Bold,
        iconFontFamily: FontFamily = XFont.THEME,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: MutableState<Boolean>,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    text = iconText,
                    textColor = if (status.value) (iconPlaneColor.activeContent
                        ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White),
                    fontSize = iconFontSize,
                    fontWeight = iconFontWeight,
                    fontFamily = iconFontFamily,
                    planeColor = if (status.value) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status.value,
                        onCheckedChange = {
                            status.value = it
                        },
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        iconText: String,
        iconFontSize: Int = 16,
        iconFontWeight: FontWeight = FontWeight.Bold,
        iconFontFamily: FontFamily = XFont.THEME,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: Boolean,
        onClick: () -> Unit = {},
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    text = iconText,
                    textColor = if (status) (iconPlaneColor.activeContent
                        ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White),
                    fontSize = iconFontSize,
                    fontWeight = iconFontWeight,
                    fontFamily = iconFontFamily,
                    planeColor = if (status) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Capsule(
        modifier: Modifier = Modifier,
        iconText: String,
        iconFontSize: Int = 16,
        iconFontWeight: FontWeight = FontWeight.Bold,
        iconFontFamily: FontFamily = XFont.THEME,
        planeSize: Int = 40,
        text: String,
        textColor: Color = Color.White,
        fontSize: Int = 14,
        subText: String? = null,
        subTextColor: Color = CONTENT_GRAY,
        subFontSize: Int = 12,
        padding: XPadding = XPadding.all(10),
        itemColor: XColorGroup = XColorGroup(
            background = BACKGROUND_GRAY,
            activeBackground = BACKGROUND_THEME_ACTIVE,
            border = BORDER_GRAY,
            activeBorder = BORDER_THEME_ACTIVE
        ),
        iconPlaneColor: XColorGroup = XColorGroup(
            background = PLANE_GRAY,
            activeBackground = Color.White,
            content = Color.White,
            activeContent = CONTENT_THEME
        ),
        borderRadius: Int = 15,
        showSwitcher: Boolean = false,
        status: Boolean,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit,
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        XCard.Switch(
            color = itemColor,
            padding = padding,
            modifier = modifier,
            borderRadius = borderRadius,
            status = status,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    text = iconText,
                    textColor = if (status) (iconPlaneColor.activeContent
                        ?: CONTENT_THEME) else (iconPlaneColor.content ?: Color.White),
                    fontSize = iconFontSize,
                    fontWeight = iconFontWeight,
                    fontFamily = iconFontFamily,
                    planeColor = if (status) iconPlaneColor.activeBackground else iconPlaneColor.background,
                    planeSize = planeSize
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = text,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            fontSize = subFontSize.sp,
                            color = subTextColor,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showSwitcher) {
                    Switch(
                        checked = status,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier
                            .height(40.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = itemColor.activeBorder
                                ?: BORDER_THEME_ACTIVE, // 选中时拇指颜色
                            checkedTrackColor = itemColor.activeBackground
                                ?: BORDER_THEME_ACTIVE, // 选中时轨道颜色
                            // uncheckedThumbColor = XBorderColor.GRAY, // 未选中时拇指颜色
                            uncheckedTrackColor = Color.Transparent, // 未选中时轨道颜色
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}