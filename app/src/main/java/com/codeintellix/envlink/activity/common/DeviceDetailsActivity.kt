package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XHeader
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.codeintellix.envlink.activity.theme.GreenWhite
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.OrangeYellow
import com.codeintellix.envlink.activity.theme.SkyBlue
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.activity.theme.Yellow
import com.codeintellix.envlink.activity.theme.YellowGreen
import com.codeintellix.envlink.entity.sensor.SensorDataVO
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
class DeviceDetailsActivity : ComponentActivity() {
    // 预览时使用的数据
    private val sensorDataVOLists = listOf(
        SensorDataVO(
            title = "环境温度",
            value = "26",
            unit = "℃",
            status = "正常",
            statusColor = LightGreen,
            progress = 0.6f,
            icon = R.drawable.ic_thermometer,
            iconColor = listOf(LightGreen, YellowGreen)
        ),
        SensorDataVO(
            title = "空气湿度",
            value = "68",
            unit = "%",
            status = "正常",
            statusColor = LightGreen,
            progress = 0.68f,
            icon = R.drawable.ic_water,
            iconColor = listOf(SkyBlue, DarkBlue)
        ),
        SensorDataVO(
            title = "光照强度",
            value = "850",
            unit = "Lux",
            status = "偏弱",
            statusColor = OrangeYellow,
            progress = 0.4f,
            icon = R.drawable.ic_sunny,
            iconColor = listOf(Yellow, OrangeYellow)
        ),
        SensorDataVO(
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
                backgroundColors = listOf(GreenWhite, Color.White, GreenWhite),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceDetailsScreen()
            }
        }
    }

    @Composable
    fun DeviceDetailsScreen() {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val startFadePx = with(LocalDensity.current) { 50.dp.toPx() } // 开始变透明的滚动阈值
        val endFadePx = with(LocalDensity.current) { 100.dp.toPx() }   // 完全透明的滚动阈值
        val showHeaderLine by remember {
            derivedStateOf { scrollState.value > endFadePx }
        }
        val plantAlpha by remember {
            derivedStateOf {
                val scroll = scrollState.value

                when {
                    scroll <= startFadePx -> 1f
                    scroll >= endFadePx -> 0f
                    else -> 1f - (scroll - startFadePx) / (endFadePx - startFadePx)
                }
            }
        }

        val scope = rememberCoroutineScope()
        val dragOffset = remember { Animatable(IntOffset(0, 0), IntOffset.VectorConverter) }
        var imageHeight by remember { mutableIntStateOf(0) }
        val currentHeightPx = (imageHeight - scrollState.value).coerceAtLeast(0)
        val currentHeightDp = with(LocalDensity.current) { currentHeightPx.toDp() }
        // val showBox = currentHeightDp.value > 150
        val showBox = plantAlpha > 0f

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
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .alpha(plantAlpha)  // 新增透明度
                        .onSizeChanged { size ->
                            imageHeight = size.height
                        }
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
                    .alpha((0.6f + (1f - plantAlpha) * 0.4f))
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
                        color = if (showHeaderLine) WhiteGray else Color.Transparent,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f)
                    )
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(
                            top = 20.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 120.dp
                        )
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    StatusArea()

                    EnvironmentArea(sensorDataVOLists)

                    ControlArea()

                    ThresholdArea()
                }
            }

            if (showBox) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 80.dp, end = 40.dp)
                        .offset { dragOffset.value }
                        .fillMaxWidth(0.35f)
                        .height(currentHeightDp)
                        // .background(Color.Black.withAlpha(0.3f))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
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
        sensorDataVOList: List<SensorDataVO>
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                sensorDataVOList.take(2).forEach { data ->
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
                sensorDataVOList.drop(2).forEach { data ->
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
        data: SensorDataVO,
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

    @Composable
    fun ControlArea() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = "设备控制",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackGray
            )

            ControlCard(
                icon = R.drawable.ic_ai,
                iconColor = LightGreen,
                title = "智能模式",
                description = "根据环境自动控制",
                status = true
            ) {}

            ControlCard(
                icon = R.drawable.ic_fan,
                iconColor = SkyBlue,
                title = "风扇",
                description = "通风系统",
                status = false,
                enabled = false
            ) {}

            ControlCard(
                icon = R.drawable.ic_bulb,
                iconColor = Yellow,
                title = "补光灯",
                description = "照明系统",
                status = true,
                enabled = false
            ) {}

            ControlCard(
                icon = R.drawable.ic_pump,
                iconColor = OrangeYellow,
                title = "水泵",
                description = "灌溉系统",
                status = false,
                enabled = false
            ) {}
        }
    }

    @Composable
    fun ControlCard(
        modifier: Modifier = Modifier,
        @DrawableRes icon: Int,
        iconColor: Color,
        title: String,
        description: String,
        status: Boolean,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        MicaCard(
            modifier = modifier.fillMaxWidth(),
            padding = XPadding.all(15),
            horizontalAlignment = Alignment.Start,
            onClick = { if (enabled) onClick }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackGray,
                            maxLines = 1
                        )

                        Text(
                            text = description,
                            fontSize = 12.sp,
                            color = Gray,
                            maxLines = 1
                        )
                    }
                }

                Switch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = status,
                    onCheckedChange = {
                        onClick
                    },
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = LightGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = WhiteGray,
                        uncheckedBorderColor = Color.Transparent,
                        // 不可用状态
                        disabledCheckedThumbColor = Color.White,
                        disabledCheckedTrackColor = LightGreen.withAlpha(0.5f),
                        disabledUncheckedThumbColor = Color.White,
                        disabledUncheckedTrackColor = WhiteGray.withAlpha(0.5f),
                        disabledUncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }

    @Composable
    fun ThresholdArea() {
        var moistureMinThreshold by remember { mutableIntStateOf(30) }
        var moistureMaxThreshold by remember { mutableIntStateOf(70) }
        var temperatureMinThreshold by remember { mutableIntStateOf(20) }
        var temperatureMaxThreshold by remember { mutableIntStateOf(30) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = "阈值设置",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackGray
            )

            ThresholdCard(
                title = "温度下限",
                description = "低于该温度关闭风扇",
                value = temperatureMinThreshold,
                minValue = 0,
                maxValue = 50,
                unit = "℃",
                onValueChange = { newValue ->
                    temperatureMinThreshold = newValue  // 更新状态
                }
            )

            ThresholdCard(
                title = "温度上限",
                description = "高于该温度开启风扇",
                value = temperatureMaxThreshold,
                minValue = 0,
                maxValue = 50,
                unit = "℃",
                onValueChange = { newValue ->
                    temperatureMaxThreshold = newValue  // 更新状态
                }
            )

            ThresholdCard(
                title = "土壤湿度下限",
                description = "低于该湿度开启水泵",
                value = moistureMinThreshold,
                minValue = 0,
                maxValue = 100,
                unit = "%",
                onValueChange = { newValue ->
                    moistureMinThreshold = newValue
                }
            )

            ThresholdCard(
                title = "土壤湿度上限",
                description = "高于该湿度关闭水泵",
                value = moistureMaxThreshold,
                minValue = 0,
                maxValue = 100,
                unit = "%",
                onValueChange = { newValue ->
                    moistureMaxThreshold = newValue
                }
            )
        }
    }

    @Composable
    fun ThresholdCard(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
        value: Int,
        minValue: Int = 0,
        maxValue: Int = 100,
        unit: String,
        onValueChange: (Int) -> Unit
    ) {
        require(minValue < maxValue) { "minValue ($minValue) must be less than maxValue ($maxValue)" }

        MicaCard(
            modifier = modifier.fillMaxWidth(),
            padding = XPadding.all(15),
            horizontalAlignment = Alignment.Start,
            onClick = {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackGray,
                            maxLines = 1
                        )

                        Text(
                            text = description,
                            fontSize = 12.sp,
                            color = Gray,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "$value$unit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen,
                        maxLines = 1
                    )
                }

                Slider(
                    value = value.toFloat(),
                    onValueChange = { newValue ->
                        onValueChange(newValue.roundToInt()) // 取整后回调
                    },
                    valueRange = minValue.toFloat()..maxValue.toFloat(), // 设置范围
                    steps = (maxValue - minValue - 1).coerceAtLeast(0), // 使滑块只能在整数位置停留
                    colors = SliderDefaults.colors(
                        thumbColor = LightGreen,
                        activeTrackColor = LightGreen,
                        inactiveTrackColor = WhiteGray,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}