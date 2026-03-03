package com.codeintellix.envlink.activity.common.widget

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
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
    padding: XPadding = XPadding.horizontal(20),
    spacing: Int = 25,
    indicatorColor: Color = LightGreen,
    indicatorPadding: XPadding = XPadding.vertical(10),
    indicatorHeight: Int = 4,
    indicatorCornerRadius: Float = 2f,
    fontSize: Int = 20,
    textColorSelected: Color = BlackGray,
    textColorUnselected: Color = Gray,
    fadeEdge: FadeEdge? = FadeEdge.Both(),
    onTabSelected: (Int) -> Unit
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = padding.top.dp,
                bottom = padding.bottom.dp
            )
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                // 1. 绘制 LazyRow 内容
                drawContent()

                // 2. 绘制指示器（如果选中项可见）
                val selectedItemVisible =
                    listState.layoutInfo.visibleItemsInfo.any { it.index == selectedTabIndex }
                if (selectedItemVisible) {
                    val y = size.height - indicatorHeight.dp.toPx()
                    drawRoundRect(
                        color = indicatorColor,
                        topLeft = Offset(animatedOffset, y),
                        size = Size(animatedWidth, indicatorHeight.dp.toPx()),
                        cornerRadius = CornerRadius(indicatorCornerRadius.dp.toPx())
                    )
                }

                // 3. 绘制透明度渐变遮罩
                fadeEdge?.let { edge ->
                    if (edge is FadeEdge.Both) {
                        val gradientBrush = Brush.horizontalGradient(
                            0f to Color.Transparent, // 最左透明
                            edge.leftEndRatio to Color.White, // 到 leftEndRatio 处变为不透明
                            edge.rightStartRatio to Color.White, // 保持不透明直到 rightStartRatio
                            edge.rightEndRatio to Color.Transparent,
                            1f to Color.Transparent // 最右透明
                        )
                        drawRect(
                            brush = gradientBrush,
                            blendMode = BlendMode.DstIn
                        )
                    }
                }
            }
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width((if ((padding.start - spacing) > 0) (padding.start - spacing) else 0).dp))
            }
            itemsIndexed(tabs) { index, title ->
                Text(
                    text = title,
                    fontSize = fontSize.sp,
                    fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickVfx { onTabSelected(index) }
                        .padding(
                            start = indicatorPadding.start.dp,
                            end = indicatorPadding.end.dp,
                            top = indicatorPadding.top.dp,
                            bottom = indicatorPadding.bottom.dp
                        )
                        .onPlaced { coordinates ->
                            val offset = coordinates.positionInParent().x
                            val width = coordinates.size.width.toFloat()
                            itemPositions[index] = offset to width
                            if (index == selectedTabIndex) {
                                targetIndicatorOffset = offset
                                targetIndicatorWidth = width
                            }
                        },
                    color = if (index == selectedTabIndex) textColorSelected else textColorUnselected
                )
            }
            item {
                Spacer(modifier = Modifier.width((if ((padding.end - spacing) > 0) (padding.end - spacing) else 0).dp))
            }
        }
    }
}

sealed class FadeEdge {
    data class Both(
        val leftEndRatio: Float = 0.2f,   // 左侧渐变结束位置（占宽度比例），到达此值后完全不透明
        val rightStartRatio: Float = 0.8f, // 右侧渐变起始位置（占宽度比例），从此处开始渐变为透明
        val rightEndRatio: Float = 0.9f // 右侧渐变结束位置（占宽度比例）
    ) : FadeEdge()
}