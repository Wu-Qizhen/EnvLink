package com.codeintellix.envlink.activity.common.widget

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codeintellix.envlink.activity.theme.LightGreen

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
@Composable
fun ScaleRefreshIndicator(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    color: Color = LightGreen
) {
    // 使用动画实现从小变大的效果
    // 当 isRefreshing 为 true 时，目标值为 1f，否则为 0f
    val scale by animateFloatAsState(
        targetValue = if (isRefreshing) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, // 弹性阻尼，产生“弹跳”感
            stiffness = Spring.StiffnessLow // 低刚度，让动画更明显
        ),
        label = "RefreshIndicatorScale"
    )

    // 只有当 scale 大于 0 时才绘制，避免占据布局空间
    if (scale > 0) {
        Box(
            modifier = modifier // 确保位于顶部
                .padding(top = 16.dp) // 距离顶部的距离
                .scale(scale) // 应用缩放动画
                .alpha(scale) // 透明度随缩放变化，更自然
                .background(color, CircleShape) // 圆形背景
                .size(48.dp), // 指示器整体大小
            contentAlignment = Alignment.Center
        ) {
            // 中间的加载动画
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White, // 加载条颜色设为白色，与背景形成对比
                strokeWidth = 2.5.dp
            )
        }
    }
}
