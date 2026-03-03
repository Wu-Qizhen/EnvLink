package com.codeintellix.envlink.activity.common.widget

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.03
 */
@SuppressLint("FrequentlyChangingValue")
@Composable
fun ScrollableRowTab(
    tabs: List<String>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    padding: XPadding = XPadding.vertical(10),
    spacing: Int = 20,
    indicatorColor: Color = LightGreen,
    indicatorHeight: Int = 4,
    indicatorCornerRadius: Float = 2f,
    fontSize: Int = 20,
    textColorSelected: Color = BlackGray,
    textColorUnselected: Color = Gray,
    onTabSelected: (Int) -> Unit,
) {
    val listState = rememberLazyListState()

    // 存储每个 Tab 项相对于父容器的 × 偏移和宽度（像素）
    val itemPositions = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }

    // 目标指示器位置和宽度（像素），由选中项的位置决定
    var targetIndicatorOffset by remember { mutableFloatStateOf(0f) }
    var targetIndicatorWidth by remember { mutableFloatStateOf(0f) }

    // 动画值：平滑过渡到目标值
    val animatedOffset by animateFloatAsState(
        targetValue = targetIndicatorOffset,
        animationSpec = tween(durationMillis = 100), label = "offset"
    )
    val animatedWidth by animateFloatAsState(
        targetValue = targetIndicatorWidth,
        animationSpec = tween(durationMillis = 100), label = "width"
    )

    // 当选中项变化时，滚动到该项（带动画）
    LaunchedEffect(selectedTabIndex) {
        listState.animateScrollToItem(selectedTabIndex)
    }

    // 判断选中项当前是否可见（用于控制是否绘制指示器）
    val selectedItemVisible =
        listState.layoutInfo.visibleItemsInfo.any { it.index == selectedTabIndex }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                // 先绘制自定义下横条（在内容下方）
                if (selectedItemVisible) {
                    val y = size.height - indicatorHeight.dp.toPx() // 底部位置
                    drawRoundRect(
                        color = indicatorColor,
                        topLeft = Offset(animatedOffset, y),
                        size = Size(animatedWidth, indicatorHeight.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                            indicatorCornerRadius.dp.toPx()
                        )
                    )
                }
                // 再绘制 LazyRow 原本的内容（Tab 文字等）
                drawContent()
            },
        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        itemsIndexed(tabs) { index, title ->
            Text(
                text = title,
                fontSize = fontSize.sp,
                fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .clickVfx { onTabSelected(index) }
                    .padding(
                        start = padding.start.dp,
                        end = padding.end.dp,
                        top = padding.top.dp,
                        bottom = padding.bottom.dp
                    )
                    // 获取每个项相对于父容器的位置
                    .onPlaced { coordinates ->
                        val offset = coordinates.positionInParent().x
                        val width = coordinates.size.width.toFloat()
                        itemPositions[index] = offset to width
                        // 如果当前项是选中项，实时更新动画目标值
                        if (index == selectedTabIndex) {
                            targetIndicatorOffset = offset
                            targetIndicatorWidth = width
                        }
                    },
                color = if (index == selectedTabIndex) textColorSelected else textColorUnselected
            )
        }
    }
}