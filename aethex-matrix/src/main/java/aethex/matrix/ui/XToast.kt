package aethex.matrix.ui

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XBorderColor
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.property.XSpacings
import aethex.matrix.foundation.type.XDeviceType
import aethex.matrix.foundation.type.XSpacingType
import aethex.matrix.ui.XBackground.TOAST_HORIZONTAL_MARGIN
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 提示栏
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.7.11
 * Updated by Wu Qizhen on 2025.6.16
 * Updated by Wu Qizhen on 2025.7.22
 */
object XToast {
    var toastContent by mutableStateOf("")
    var snackBarObject: SnackBarObject? by mutableStateOf(null)

    /**
     * 显示文本
     *
     * @param content 显示内容
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    fun showText(content: String) {
        toastContent = content
    }

    /**
     * 显示文本
     *
     * @param context 上下文
     * @param content 显示内容
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    fun showText(context: Context, @StringRes content: Int) {
        context.let {
            toastContent = it.getString(content)
        }
    }

    /**
     * 显示 snackBar
     *
     * @param content 显示内容
     * @param leadingIcon 显示图标
     * @param trailingIcon 显示图标
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    fun showSnackBar(
        content: String,
        leadingIcon: ImageVector,
        trailingIcon: ImageVector,
        onClick: () -> Unit
    ) {
        snackBarObject = SnackBarObject(
            text = content,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            onClick = onClick
        )
    }

    /**
     * ▼ 内容显示
     *
     * @param content 显示内容
     * @param deviceType 设备类型
     * @param toastMargin 间距
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    @Composable
    fun ToastContent(
        content: String,
        deviceType: XDeviceType = XDeviceType.DEFAULT,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt())
    ) {
        val size = when (deviceType) {
            XDeviceType.WEARABLE -> 12
            XDeviceType.PHONE -> 14
            XDeviceType.TABLET -> 16
            XDeviceType.DESKTOP -> 16
        }

        Box(
            modifier = Modifier
                .padding(
                    start = toastMargin.start.dp,
                    end = toastMargin.end.dp,
                    bottom = toastMargin.bottom.dp
                )
                .fillMaxWidth()
                .border(
                    width = 0.3f.dp,
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.linearGradient(
                        XBorderColor.GRAY_GRADIENT,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.withAlpha(0.5f),
                            0.5f to Color.Black.withAlpha(0.5f),
                            1f to XThemeColor.BASE
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = content,
                fontSize = size.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = size.dp, vertical = size.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }

    /**
     * ▼ 条目显示
     *
     * @param snackBarObject 显示内容
     * @param deviceType 设备类型
     * @param toastMargin 间距
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    @Composable
    fun SnackBarContent(
        snackBarObject: SnackBarObject,
        deviceType: XDeviceType = XDeviceType.DEFAULT,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt())
    ) {
        val localDensity = LocalDensity.current
        val size = when (deviceType) {
            XDeviceType.WEARABLE -> 12
            XDeviceType.PHONE -> 14
            XDeviceType.TABLET -> 16
            XDeviceType.DESKTOP -> 16
        }

        Row(
            modifier = Modifier
                .clickVfx {
                    snackBarObject.onClick()
                    XToast.snackBarObject = null
                }
                .padding(
                    start = toastMargin.start.dp,
                    end = toastMargin.end.dp,
                    bottom = toastMargin.bottom.dp
                )
                .fillMaxWidth()
                .border(
                    width = 0.3f.dp,
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.linearGradient(
                        XBorderColor.GRAY_GRADIENT,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.withAlpha(0.5f),
                            .5f to Color.Black.withAlpha(0.5f),
                            1f to XThemeColor.BASE
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = snackBarObject.leadingIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(
                    with(localDensity) { 15.sp.toDp() }
                )
            )

            Spacer(modifier = Modifier.width(XSpacings.getSpacing(XSpacingType.COMPONENT_S)))

            Text(
                text = snackBarObject.text,
                // fontFamily = FontFamily,
                fontSize = size.sp,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium
            )

            Icon(
                imageVector = snackBarObject.trailingIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(
                    with(localDensity) { size.sp.toDp() }
                )
            )
        }
    }

    /**
     * 条目对象
     *
     * @param text 显示内容
     * @param leadingIcon 显示图标
     * @param trailingIcon 显示图标
     * @param onClick 点击事件
     *
     * Created by Wu Qizhen on 2024.7.11
     * Updated by Wu Qizhen on 2025.6.16
     * Updated by Wu Qizhen on 2025.7.22
     */
    data class SnackBarObject(
        val text: String,
        val leadingIcon: ImageVector,
        val trailingIcon: ImageVector,
        val onClick: () -> Unit
    )
}

/*
@preview
@Composable
fun ToastContentPreview() {
    XTheme {
        XToast.ToastContent(content = "这是一条消息")
    }
}*/