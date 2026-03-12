package aethex.matrix.ui

import aethex.matrix.animation.XVisibility.ScaleFade
import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.property.XSpacings
import aethex.matrix.foundation.theme.XTheme
import android.annotation.SuppressLint
import android.graphics.PointF
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.random.Random

/**
 * 背景
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.6.16
 * Refactored by Wu Qizhen on 2024.11.30
 * Updated by Wu Qizhen on 2025.6.16
 * Updated by Wu Qizhen on 2025.7.23
 * Updated by Wu Qizhen on 2026.1.13
 */
object XBackground {
    /**
     * 标题栏背景水平间距
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2025.7.23
     */
    // @Composable
    // fun backgroundHorizontalPadding() = 20.dp
    // fun backgroundHorizontalPadding() = if (XStats.isScreenRound()) 20.dp else 15.dp
    const val TOAST_HORIZONTAL_MARGIN = 20

    /**
     * 渐变背景
     *
     * @param content 内容
     *
     * Created by Wu Qizhen on 2026.3.2
     */
    @Composable
    fun Gradient(
        backgroundColors: List<Color>,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        content: @Composable () -> Unit
    ) {
        XTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(backgroundColors))
            ) {
                content()

                // 提示系统
                // 1. Toast 提示
                ScaleFade(
                    visible = XToast.toastContent.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.ToastContent(
                        content = XToast.toastContent,
                        toastMargin = toastMargin
                    )
                }

                // 2. SnackBar 提示
                ScaleFade(
                    visible = XToast.snackBarObject != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.snackBarObject?.let {
                        XToast.SnackBarContent(
                            it,
                            toastMargin = toastMargin
                        )
                    }
                }

                // 3. 自动隐藏逻辑
                LaunchedEffect(key1 = XToast.toastContent) {
                    if (XToast.toastContent.isNotEmpty()) {
                        delay(2000)
                        XToast.toastContent = ""
                    }
                }

                LaunchedEffect(key1 = XToast.snackBarObject) {
                    if (XToast.snackBarObject != null) {
                        delay(2000)
                        XToast.snackBarObject = null
                    }
                }
            }
        }
    }

    /**
     * 圆形背景
     *
     * @param content 内容
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2025.7.23
     * Deprecated on 2026.1.13
     */
    @Composable
    fun Circles(
        circleColor: Color = XThemeColor.BASE,
        backgroundColor: Color = Color.Black,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        content: @Composable () -> Unit
    ) {
        XTheme {
            // var bottomWidth by remember { mutableStateOf(0.dp) }
            // var topWidth by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current
            val ambientAlpha = 0.6f

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                var circleHeight by remember {
                    mutableStateOf(0.dp)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = circleHeight * 0.5f, x = circleHeight * -0.5f)
                        .fillMaxWidth()
                        .scale(scaleX = 1.1f, scaleY = 1.1f)
                        .aspectRatio(1f)
                        .alpha(ambientAlpha * 0.9f)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    circleColor,
                                    Color.Transparent
                                )
                            )
                        )
                        .onSizeChanged {
                            circleHeight = with(localDensity) { it.height.toDp() }
                        }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = circleHeight * -0.5f, x = circleHeight * 0.5f)
                        .fillMaxWidth()
                        .scale(scaleX = 1.1f, scaleY = 1.1f)
                        .aspectRatio(1f)
                        .alpha(ambientAlpha * 0.9f)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    circleColor,
                                    Color.Transparent
                                )
                            )
                        )
                        .onSizeChanged {
                            circleHeight = with(localDensity) { it.height.toDp() }
                        }
                )

                /*Column(
                    Modifier
                        .fillMaxSize()
                ) {*/
                content()
                /*}*/

                // 提示系统
                // 1. Toast 提示
                ScaleFade(
                    visible = XToast.toastContent.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.ToastContent(
                        content = XToast.toastContent,
                        toastMargin = toastMargin
                    )
                }

                // 2. SnackBar 提示
                ScaleFade(
                    visible = XToast.snackBarObject != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.snackBarObject?.let {
                        XToast.SnackBarContent(
                            it,
                            toastMargin = toastMargin
                        )
                    }
                }

                // 3. 自动隐藏逻辑
                LaunchedEffect(key1 = XToast.toastContent) {
                    if (XToast.toastContent.isNotEmpty()) {
                        delay(2000)
                        XToast.toastContent = ""
                    }
                }

                LaunchedEffect(key1 = XToast.snackBarObject) {
                    if (XToast.snackBarObject != null) {
                        delay(2000)
                        XToast.snackBarObject = null
                    }
                }
            }
        }
    }

    /**
     * 呼吸背景
     *
     * @param activeColor 主颜色
     * @param toastMargin 吐司水平间距
     * @param isBreathing 是否呼吸（加载）
     * @param content 内容
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2025.7.23
     * Updated by Wu Qizhen on 2026.1.13
     */
    @SuppressLint("UseOfNonLambdaOffsetOverload")
    @Composable
    fun Breathing(
        activeColor: Color = XBackgroundColor.THEME_LIGHT,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        isBreathing: Boolean = false,
        content: @Composable () -> Unit
    ) {
        XTheme {
            val localDensity = LocalDensity.current
            val ambientAlpha = 0.6f
            val infiniteTransition = rememberInfiniteTransition(label = "")
            // 呼吸动画状态控制
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        0.7f at 500
                    },
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                var circleHeight by remember {
                    mutableStateOf(0.dp)
                }

                // 呼吸效果背景层
                Box(
                    modifier = Modifier
                        .offset(y = circleHeight * -0.5f)
                        .fillMaxWidth()
                        .scale(scaleX = 1.1f, scaleY = 1.1f)
                        .aspectRatio(1f)
                        .alpha(if (isBreathing) ambientAlpha * alpha * 0.9f else ambientAlpha * 0.9f)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    activeColor,
                                    Color.Transparent
                                )
                            )
                        )
                        .onSizeChanged {
                            circleHeight = with(localDensity) { it.height.toDp() }
                        }
                )

                content()

                // 提示系统
                // 1. Toast 提示
                ScaleFade(
                    visible = XToast.toastContent.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.ToastContent(
                        content = XToast.toastContent,
                        toastMargin = toastMargin
                    )
                }

                // 2. SnackBar 提示
                ScaleFade(
                    visible = XToast.snackBarObject != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.snackBarObject?.let {
                        XToast.SnackBarContent(
                            it,
                            toastMargin = toastMargin
                        )
                    }
                }

                // 3. 自动隐藏逻辑
                LaunchedEffect(key1 = XToast.toastContent) {
                    if (XToast.toastContent.isNotEmpty()) {
                        delay(2000)
                        XToast.toastContent = ""
                    }
                }

                LaunchedEffect(key1 = XToast.snackBarObject) {
                    if (XToast.snackBarObject != null) {
                        delay(2000)
                        XToast.snackBarObject = null
                    }
                }
            }
        }
    }

    /**
     * 呼吸背景
     *
     * @param title 标题
     * @param activeColor 主颜色
     * @param toastMargin 吐司水平间距
     * @param isBreathing 是否呼吸（加载）
     * @param modifier 修饰符
     * @param horizontalAlignment 水平对齐
     * @param content 内容
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Breathing(
        title: String,
        activeColor: Color = XBackgroundColor.THEME_LIGHT,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        isBreathing: Boolean = false,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.padding(horizontal = XSpacings.getComponentSpacing()),
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        content: @Composable () -> Unit
    ) {
        Breathing(
            isBreathing = isBreathing,
            activeColor = activeColor,
            toastMargin = toastMargin
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = horizontalAlignment
            ) {
                XBar.Text(title = title)

                content()
            }
        }
    }

    /**
     * 呼吸背景
     *
     * @param title 标题资源
     * @param activeColor 主颜色
     * @param toastMargin 吐司水平间距
     * @param isBreathing 是否呼吸（加载）
     * @param modifier 修饰符
     * @param horizontalAlignment 水平对齐
     * @param content 内容
     *
     * Created by Wu Qizhen on 2024.6.16
     * Updated by Wu Qizhen on 2025.7.23
     */
    @Composable
    fun Breathing(
        title: Int,
        activeColor: Color = XBackgroundColor.THEME_LIGHT,
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        isBreathing: Boolean = false,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.padding(horizontal = XSpacings.getComponentSpacing()),
        horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        content: @Composable () -> Unit
    ) {
        Breathing(
            title = stringResource(id = title),
            activeColor = activeColor,
            toastMargin = toastMargin,
            isBreathing = isBreathing,
            modifier = modifier,
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    /**
     * 深空背景
     *
     * @param activeColor 主颜色
     * @param secondaryColor 次要颜色
     * @param tertiaryColor 三级颜色
     * @param toastMargin Toast 提示的边距
     * @param content 内容
     *
     * Created by Wu Qizhen on 2026.1.11
     */
    @Composable
    fun Space(
        backgroundColor: Color = Color.Black, // Color(12, 18, 24)
        activeColor: Color = XBackgroundColor.THEME_LIGHT,
        secondaryColor: Color? = Color(236, 72, 153),
        tertiaryColor: Color? = Color(59, 130, 246),
        toastMargin: XPadding = XPadding.horizontal(TOAST_HORIZONTAL_MARGIN)
            .bottom(XSpacings.getComponentSpacing().value.toInt()),
        content: @Composable () -> Unit
    ) {
        XTheme {
            val localDensity = LocalDensity.current
            val ambientAlpha = 0.6f
            var circleHeight by remember {
                mutableStateOf(0.dp)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)  // 深空蓝黑色背景
            ) {
                // 左上角的紫色光晕
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-100).dp, y = (-80).dp)
                        .size(300.dp)
                        // .fillMaxWidth()
                        // .scale(scaleX = 1.2f, scaleY = 1.2f)
                        .aspectRatio(1f)
                        .alpha(ambientAlpha)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    // Color(139, 92, 246),
                                    activeColor,
                                    Color.Transparent
                                )
                            )
                        )
                )

                // 右下角渐变
                if (secondaryColor != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 100.dp, y = (-200).dp)
                            .size(300.dp)
                            .scale(scaleX = 2f, scaleY = 2f)
                            .aspectRatio(1f)
                            .alpha(ambientAlpha * 0.4f)
                            .background(
                                shape = CircleShape, brush = Brush.radialGradient(
                                    listOf(
                                        secondaryColor,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // 底部的蓝色光晕
                if (tertiaryColor != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(y = circleHeight * 0.3f, x = circleHeight * -0.3f)
                            .fillMaxWidth()
                            .scale(scaleX = 1.2f, scaleY = 1.2f)
                            .aspectRatio(1f)
                            .alpha(ambientAlpha)
                            .background(
                                shape = CircleShape, brush = Brush.radialGradient(
                                    listOf(
                                        tertiaryColor,
                                        Color.Transparent
                                    )
                                )
                            )
                            .onSizeChanged {
                                circleHeight = with(localDensity) { it.height.toDp() }
                            }
                    )
                }

                // 左上角渐变
                /*Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = circleHeight * -0.1f, x = circleHeight * -0.3f)
                        .fillMaxWidth()
                        .scale(scaleX = 2f, scaleY = 2f)
                        .aspectRatio(1f)
                        .alpha(ambientAlpha * 0.2f)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    Color(236, 72, 153),
                                    Color.Transparent
                                )
                            )
                        )
                        .onSizeChanged {
                            circleHeight = with(localDensity) { it.height.toDp() }
                        }
                )*/

                // 内容层
                content()

                // 提示系统
                // 1. Toast 提示
                ScaleFade(
                    visible = XToast.toastContent.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.ToastContent(
                        content = XToast.toastContent,
                        toastMargin = toastMargin
                    )
                }

                // 2. SnackBar 提示
                ScaleFade(
                    visible = XToast.snackBarObject != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.snackBarObject?.let {
                        XToast.SnackBarContent(
                            it,
                            toastMargin = toastMargin
                        )
                    }
                }

                // 3. 自动隐藏逻辑
                LaunchedEffect(key1 = XToast.toastContent) {
                    if (XToast.toastContent.isNotEmpty()) {
                        delay(2000)
                        XToast.toastContent = ""
                    }
                }

                LaunchedEffect(key1 = XToast.snackBarObject) {
                    if (XToast.snackBarObject != null) {
                        delay(2000)
                        XToast.snackBarObject = null
                    }
                }
            }
        }
    }

    /**
     *  节点配色
     *
     *  Created by Wu Qizhen on 2026.1.15
     */
    object SciFiConfig {
        val DeepSpaceBg = Color.Black // 深背景色
        val NeonCyan = Color.Green    // 霓虹青色（主要高光）
        val NeonPurple = Color(31, 1, 51)  // 霓虹紫色（次要高光）
        val ParticleColor = NeonCyan.copy(alpha = 0.7f)
        val LineColorBase = NeonCyan

        const val NODE_COUNT = 40           // 节点数量，根据性能调整
        const val MAX_DISTANCE = 500f       // 最大连线距离
        const val NODE_SPEED_RANGE = 1.5f   // 节点移动速度因子
    }

    /**
     * 粒子节点数据结构
     *
     * Created by Wu Qizhen on 2026.1.15
     */
    data class Node(
        var position: PointF,
        var velocity: PointF,
        val radius: Float
    )

    /**
     * 粒子网络背景
     *
     * Created by Wu Qizhen on 2026.1.16
     */
    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    fun ParticleNetwork(modifier: Modifier = Modifier) {
        val nodes = remember { mutableStateListOf<Node>() }
        var isInitialized by remember { mutableStateOf(false) }

        // 添加一个动画帧计数器来触发重绘
        val frameCounter by rememberInfiniteTransition(label = "particleAnimation").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(16, easing = LinearEasing), // 约 60fps
                repeatMode = RepeatMode.Restart
            ), label = "frameCounter"
        )

        XTheme {
            BoxWithConstraints(modifier = modifier.background(SciFiConfig.DeepSpaceBg)) {
                val density = LocalDensity.current.density
                val canvasWidth = maxWidth.value * density
                val canvasHeight = maxHeight.value * density

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // 使用 frameCounter 触发重绘（即使不直接使用这个值）
                    // 这确保 Canvas 会持续重绘
                    frameCounter

                    // 初始化节点
                    if (!isInitialized && canvasWidth > 0 && canvasHeight > 0) {
                        repeat(SciFiConfig.NODE_COUNT) {
                            nodes.add(
                                Node(
                                    position = PointF(
                                        Random.nextFloat() * size.width, // 使用实际画布大小
                                        Random.nextFloat() * size.height
                                    ),
                                    velocity = PointF(
                                        (Random.nextFloat() - 0.5f) * SciFiConfig.NODE_SPEED_RANGE,
                                        (Random.nextFloat() - 0.5f) * SciFiConfig.NODE_SPEED_RANGE
                                    ),
                                    // radius = Random.nextFloat() * 5f + 3f
                                    radius = Random.nextFloat() * 7f + 5f
                                )
                            )
                        }
                        isInitialized = true
                    }

                    if (!isInitialized) return@Canvas

                    // 更新物理逻辑
                    nodes.forEach { node ->
                        node.position.x += node.velocity.x
                        node.position.y += node.velocity.y

                        // 边界反弹（添加边界缓冲避免卡在边界）
                        val bounceFactor = 0.98f
                        if (node.position.x <= 0) {
                            node.position.x = 0f
                            node.velocity.x = abs(node.velocity.x) * bounceFactor
                        } else if (node.position.x >= size.width) {
                            node.position.x = size.width
                            node.velocity.x = -abs(node.velocity.x) * bounceFactor
                        }

                        if (node.position.y <= 0) {
                            node.position.y = 0f
                            node.velocity.y = abs(node.velocity.y) * bounceFactor
                        } else if (node.position.y >= size.height) {
                            node.position.y = size.height
                            node.velocity.y = -abs(node.velocity.y) * bounceFactor
                        }
                    }

                    // 绘制逻辑
                    // 1. 绘制连线（O(N^2)）
                    for (i in nodes.indices) {
                        for (j in i + 1 until nodes.size) {
                            val nodeA = nodes[i]
                            val nodeB = nodes[j]
                            val dx = nodeA.position.x - nodeB.position.x
                            val dy = nodeA.position.y - nodeB.position.y
                            val distance = hypot(dx, dy)

                            if (distance < SciFiConfig.MAX_DISTANCE) {
                                // 距离越近越亮
                                val alpha = (1f - distance / SciFiConfig.MAX_DISTANCE).pow(2)
                                    .coerceIn(0f, 1f)

                                drawLine(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            SciFiConfig.LineColorBase.copy(alpha = alpha),
                                            SciFiConfig.NeonPurple.copy(alpha = alpha * 0.5f)
                                        ),
                                        start = Offset(nodeA.position.x, nodeA.position.y),
                                        end = Offset(nodeB.position.x, nodeB.position.y)
                                    ),
                                    start = Offset(nodeA.position.x, nodeA.position.y),
                                    end = Offset(nodeB.position.x, nodeB.position.y),
                                    strokeWidth = 3.0f * alpha,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // 2. 绘制节点点
                    nodes.forEach { node ->
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    SciFiConfig.ParticleColor,
                                    SciFiConfig.ParticleColor.copy(alpha = 0.3f),
                                    SciFiConfig.ParticleColor.copy(alpha = 0f)
                                ),
                                center = Offset(node.position.x, node.position.y),
                                // radius = node.radius * 3
                                radius = node.radius * 5
                            ),
                            radius = node.radius,
                            center = Offset(node.position.x, node.position.y)
                        )
                    }
                }
            }
        }
    }
}