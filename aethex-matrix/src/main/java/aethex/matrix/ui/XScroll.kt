package aethex.matrix.ui

import aethex.matrix.foundation.property.XSpacings
import aethex.matrix.foundation.type.XSpacingType
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 滚动栏
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.9.7
 */
object XScroll {
    /**
     * 纵向滚动栏
     *
     * @param horizontalAlignment 横向对齐方式
     * @param verticalArrangement 纵向排列方式
     * @param horizontalPadding 横向内间距
     * @param enabledTopSpacing 是否启用顶部间距
     * @param isExpandToStatusBar 是否扩展到状态栏
     * @param modifier 修改器
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.9.7
     */
    @Composable
    fun Vertical(
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        verticalArrangement: Arrangement.Vertical = Arrangement.Center,
        horizontalPadding: Dp = XSpacings.getSpacing(spacingType = XSpacingType.COMPONENT_L),
        // deviceType: XDeviceType = XDeviceType.PHONE, 已在 XSpacings 实现自动获取
        enabledTopSpacing: Boolean = false,
        isExpandToStatusBar: Boolean = false,
        modifier: Modifier = Modifier.fillMaxSize(),
        content: @Composable () -> Unit
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = if (enabledTopSpacing) XSpacings.getTopSpacing(isExpandToStatusBar = isExpandToStatusBar) else 0.dp,
                    bottom = XSpacings.getBottomSpacing()
                ),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    }

    /**
     * 横向滚动栏
     *
     * @param verticalAlignment 纵向对齐方式
     * @param horizontalArrangement 横向排列方式
     * @param verticalPadding 纵向内间距
     * @param enabledTopSpacing 是否启用顶部间距
     * @param isExpandToStatusBar 是否扩展到状态栏
     * @param modifier 修改器
     * @param content 内容
     *
     * Created by Wu Qizhen on 2025.9.7
     */
    @Composable
    fun Horizontal(
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
        verticalPadding: Dp = 20.dp,
        // deviceType: XDeviceType = XDeviceType.PHONE, 已在 XSpacings 实现自动获取
        enabledTopSpacing: Boolean = false,
        isExpandToStatusBar: Boolean = false,
        modifier: Modifier = Modifier.fillMaxSize(),
        content: @Composable () -> Unit
    ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = modifier
                .horizontalScroll(scrollState)
                .padding(
                    start = verticalPadding,
                    end = verticalPadding,
                    top = if (enabledTopSpacing) XSpacings.getTopSpacing(isExpandToStatusBar = isExpandToStatusBar) else 0.dp,
                    bottom = XSpacings.getBottomSpacing()
                ),
            verticalAlignment = verticalAlignment,
            horizontalArrangement = horizontalArrangement
        ) {
            content()
        }
    }
}