package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XHeader
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.DarkBlue
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.GrayWhite
import com.codeintellix.envlink.activity.theme.Green
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.OrangeYellow
import com.codeintellix.envlink.activity.theme.SkyBlue
import com.codeintellix.envlink.activity.theme.SoftGreen
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.activity.theme.Yellow
import com.codeintellix.envlink.entity.SensorData
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
class DeviceActivity : ComponentActivity() {
    // 预览时使用的数据
    private val sensorDataList = listOf(
        SensorData(
            title = "环境温度",
            value = "26",
            unit = "℃",
            status = "正常",
            statusColor = LightGreen,
            progress = 0.6f,
            icon = R.drawable.ic_thermometer,
            iconColor = listOf(LightGreen, Green)
        ),
        SensorData(
            title = "空气湿度",
            value = "68",
            unit = "%",
            status = "正常",
            statusColor = LightGreen,
            progress = 0.68f,
            icon = R.drawable.ic_water,
            iconColor = listOf(SkyBlue, DarkBlue)
        ),
        SensorData(
            title = "光照强度",
            value = "850",
            unit = "Lux",
            status = "偏弱",
            statusColor = OrangeYellow,
            progress = 0.4f,
            icon = R.drawable.ic_sunny,
            iconColor = listOf(Yellow, OrangeYellow)
        ),
        SensorData(
            title = "土壤湿度",
            value = "45",
            unit = "%",
            status = "偏干",
            statusColor = OrangeYellow,
            progress = 0.4f,
            icon = R.drawable.ic_moisture,
            iconColor = listOf(OrangeYellow, OrangeRed)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XBackground.Gradient(
                backgroundColors = listOf(SoftGreen, Color.White, SoftGreen),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceScreen()
            }
        }
    }

    @Composable
    fun DeviceScreen() {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val isScrolled by remember {
            derivedStateOf { scrollState.value > 0 }
        }

        // val localDensity = LocalDensity.current
        val ambientAlpha = 0.6f
        // var circleHeight by remember { mutableStateOf(0.dp) }

        // 拖拽回弹效果
        val scope = rememberCoroutineScope()
        val dragOffset = remember { Animatable(IntOffset(0, 0), IntOffset.VectorConverter) }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 40.dp)
                    .offset { dragOffset.value }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_plant),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.35f)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-100).dp, y = (-100).dp)
                    .size(300.dp)
                    // .fillMaxWidth()
                    // .scale(scaleX = 1.2f, scaleY = 1.2f)
                    .aspectRatio(1f)
                    .alpha(ambientAlpha)
                    .background(
                        shape = CircleShape, brush = Brush.radialGradient(
                            listOf(
                                Yellow,
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(systemBarPadding)
            ) {
                XHeader.BackText(
                    text = stringResource(R.string.microenvironment_controller),
                    textColor = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28,
                    iconColor = Color.Black,
                    headerPadding = XPadding.all(20)
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    drawLine(
                        color = if (isScrolled) WhiteGray else Color.Transparent,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f)
                    )
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(
                            top = 10.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 120.dp
                        )
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    StatusArea()

                    EnvironmentArea(sensorDataList)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 40.dp)
                    .offset { dragOffset.value }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                // 回弹效果，释放后使用 Spring 动力学弹回原位
                                scope.launch {
                                    dragOffset.animateTo(
                                        targetValue = IntOffset(0, 0),
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // 跟随手指移动
                                scope.launch {
                                    dragOffset.snapTo(
                                        IntOffset(
                                            (dragOffset.value.x + dragAmount.x).toInt(),
                                            (dragOffset.value.y + dragAmount.y).toInt()
                                        )
                                    )
                                }
                            }
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_plant),
                    contentDescription = null,
                    alpha = 0f, // 完全透明
                    modifier = Modifier.fillMaxWidth(0.35f)
                )
            }
        }
    }

    @Composable
    fun StatusArea() {
        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "环境良好",
                fontSize = 36.sp,
                fontWeight = FontWeight.Thin,
                color = LightGreen
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "设备运行正常",
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin,
                color = BlackGray
            )
            Text(
                text = "已运行 72 小时",
                fontSize = 14.sp,
                fontWeight = FontWeight.Thin,
                color = Gray
            )
        }
    }

    @Composable
    fun EnvironmentArea(
        sensorDataList: List<SensorData>
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                sensorDataList.take(2).forEach { data ->
                    EnvironmentCard(
                        data = data,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                sensorDataList.drop(2).forEach { data ->
                    EnvironmentCard(
                        data = data,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    fun EnvironmentCard(
        data: SensorData,
        modifier: Modifier = Modifier
    ) {
        MicaCard(
            modifier = modifier.fillMaxWidth(),
            padding = XPadding.all(15),
            horizontalAlignment = Alignment.Start,
            onClick = {}
        ) {
            // 第一行：图标 + 状态标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 图标背景
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            data.iconColor[0]
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(data.icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 状态标签
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(data.statusColor.withAlpha(0.2f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = data.status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = data.statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // 传感器名称
            Text(
                text = data.title,
                fontSize = 16.sp,
                color = Gray,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(5.dp))

            // 数值 + 单位
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = data.value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackGray,
                    lineHeight = 28.sp
                )
                Text(
                    text = data.unit,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = BlackGray,
                    modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 进度条
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GrayWhite)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(data.progress)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            brush = Brush.horizontalGradient(data.iconColor)
                        )
                )
            }
        }
    }
}