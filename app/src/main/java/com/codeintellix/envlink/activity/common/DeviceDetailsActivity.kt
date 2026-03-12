package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XCard
import aethex.matrix.ui.XDialog
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XIcon
import aethex.matrix.ui.XItem
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.widget.AliveTextField
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.common.widget.ScaleRefreshIndicator
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.GrayWhite
import com.codeintellix.envlink.activity.theme.GreenWhite
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.OrangeYellow
import com.codeintellix.envlink.activity.theme.SkyBlue
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.activity.theme.Yellow
import com.codeintellix.envlink.data.device.DeviceDetailViewModel
import com.codeintellix.envlink.data.device.DeviceDetailViewModelFactory
import com.codeintellix.envlink.entity.actuator.ActuatorState
import com.codeintellix.envlink.entity.actuator.ActuatorType
import com.codeintellix.envlink.entity.cosnt.ActivityExtra
import com.codeintellix.envlink.entity.device.ConnectionState
import com.codeintellix.envlink.entity.protocol.CalibrationType
import com.codeintellix.envlink.entity.protocol.ControlMode
import com.codeintellix.envlink.entity.protocol.ControlParams
import com.codeintellix.envlink.entity.protocol.EnvironmentState
import com.codeintellix.envlink.entity.protocol.SystemInfo
import com.codeintellix.envlink.entity.sensor.CalibrationStep
import com.codeintellix.envlink.entity.sensor.SensorDataVO
import com.codeintellix.envlink.entity.sensor.SensorStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
class DeviceDetailsActivity : ComponentActivity() {
    /*// 预览时使用的数据
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
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deviceAddress = intent.getStringExtra(ActivityExtra.DEVICE_ADDRESS_EXTRA) ?: ""

        enableEdgeToEdge()
        setContent {
            XBackground.Gradient(
                backgroundColors = listOf(GreenWhite, Color.White, GreenWhite),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceDetailsScreen(
                    deviceAddress = deviceAddress
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceDetailsScreen(
        deviceAddress: String = ""
    ) {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val scrollState = rememberScrollState()
        val startFadePx = with(LocalDensity.current) { 50.dp.toPx() } // 开始变透明的滚动阈值
        val endFadePx = with(LocalDensity.current) { 100.dp.toPx() } // 完全透明的滚动阈值
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

        var isRefreshing by remember { mutableStateOf(false) }
        val pullToRefreshState = rememberPullToRefreshState()

        val viewModel: DeviceDetailViewModel = viewModel(
            factory = DeviceDetailViewModelFactory(application, deviceAddress)
        )
        val device by viewModel.device.collectAsState()
        val connectionState by viewModel.connectionState.collectAsState()
        val isConnected = connectionState is ConnectionState.Connected
        val systemInfo by viewModel.systemInfo.collectAsState()
        val sensorData by viewModel.sensorDataList.collectAsState()
        val controlMode by viewModel.controlMode.collectAsState()
        val pumpState by viewModel.pumpState.collectAsState()
        val fanState by viewModel.fanState.collectAsState()
        val lightState by viewModel.lightState.collectAsState()
        val pumpLoading by viewModel.pumpOperationLoading.collectAsState()
        val fanLoading by viewModel.fanOperationLoading.collectAsState()
        val lightLoading by viewModel.lightOperationLoading.collectAsState()
        val controlParams by viewModel.controlParams.collectAsState()
        val draftParams by viewModel.draftParams.collectAsState()
        val isParamsChanged by viewModel.isParamsChanged.collectAsState()
        val paramsLoading by viewModel.paramsLoading.collectAsState()

        // 智能评估结论
        val assessment = remember(sensorData, isConnected) {
            if (!isConnected || sensorData.any { it.status == SensorStatus.UNKNOWN }) {
                EnvironmentState.UNKNOWN
            } else {
                val abnormalStatuses = sensorData.filter { it.status != SensorStatus.NORMAL }
                if (abnormalStatuses.isEmpty()) {
                    EnvironmentState.GOOD
                } else {
                    EnvironmentState.WARN
                }
            }
        }

        /*// 当连接成功或手动刷新时触发数据获取
        LaunchedEffect(connectionState) {
            if (connectionState is ConnectionState.Connected) {
                // 连接成功后立即获取一次数据（定时器会自动开始）
            }
        }*/

        // Toast 观察
        LaunchedEffect(viewModel) {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@DeviceDetailsActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

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
                    text = device?.name ?: stringResource(R.string.microenvironment_controller),
                    textColor = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28,
                    iconColor = Color.Black,
                    headerPadding = XPadding.all(20),
                    content = {
                        XIcon.RoundPlane(
                            icon = R.drawable.ic_application,
                            iconSize = 30,
                            planeSize = 30,
                            color = XColorGroup(
                                background = Color.Transparent,
                                activeBackground = Color.Transparent,
                                content = Color.Black,
                                activeContent = Gray,
                                border = Color.Transparent
                            ),
                            onClick = {
                                val intent = Intent(
                                    this@DeviceDetailsActivity,
                                    DeviceSettingsActivity::class.java
                                )
                                intent.putExtra(
                                    ActivityExtra.DEVICE_ADDRESS_EXTRA,
                                    deviceAddress
                                )
                                startActivity(intent)
                            }
                        )
                    }
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

                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        viewModel.refresh()
                        // 延迟关闭（实际应在数据返回后关闭）
                        viewModel.viewModelScope.launch {
                            delay(1000)
                            isRefreshing = false
                        }
                    },
                    state = pullToRefreshState,
                    modifier = Modifier.fillMaxSize(),
                    indicator = {
                        val displayProgress =
                            if (isRefreshing) 1f else pullToRefreshState.distanceFraction
                        ScaleRefreshIndicator(
                            progress = displayProgress,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(
                                top = 20.dp,
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 120.dp
                            )
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        StatusArea(
                            isConnected = isConnected,
                            systemInfo = systemInfo,
                            assessment = assessment
                        )

                        EnvironmentArea(sensorDataVOList = sensorData)

                        ControlArea(
                            isConnected = isConnected,
                            autoModeEnabled = isConnected && controlMode == ControlMode.AUTO,        // 自动模式（值为0）显示开启
                            pumpState = pumpState,
                            fanState = fanState,
                            lightState = lightState,
                            pumpLoading = pumpLoading,
                            fanLoading = fanLoading,
                            lightLoading = lightLoading,
                            onAutoModeToggle = { checked ->
                                viewModel.setControlMode(if (checked) ControlMode.AUTO else ControlMode.MANUAL)
                            },
                            onPumpToggle = { newState ->
                                viewModel.setActuator(
                                    ActuatorType.PUMP,
                                    if (newState) ActuatorState.ON else ActuatorState.OFF
                                )
                            },
                            onFanToggle = { newState ->
                                viewModel.setActuator(
                                    ActuatorType.FAN,
                                    if (newState) ActuatorState.ON else ActuatorState.OFF
                                )
                            },
                            onLightToggle = { newState ->
                                viewModel.setActuator(
                                    ActuatorType.LIGHT,
                                    if (newState) ActuatorState.ON else ActuatorState.OFF
                                )
                            }
                        )

                        ThresholdArea(
                            viewModel = viewModel,
                            draftParams = draftParams,
                            isParamsChanged = isParamsChanged
                        )

                        CalibrationArea(
                            viewModel = viewModel,
                            isConnected = isConnected
                        )
                    }
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

            if (isParamsChanged) {
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.Center
                ) {
                    XCard.Lively(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        borderRadius = 30,
                        color = XColorGroup(
                            background = LightGreen,
                            activeBackground = LightGreen.withAlpha(0.8f)
                        ),
                        padding = XPadding.horizontal(15).vertical(10),
                        onClick = {
                            if (!paramsLoading) {
                                viewModel.saveControlParams()
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (paramsLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Text(
                                text = "保存设置",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StatusArea(
        isConnected: Boolean,
        systemInfo: SystemInfo?,
        assessment: EnvironmentState
    ) {
        val uptimeText = remember(systemInfo) {
            systemInfo?.uptimeSeconds?.let { formatUptime(it) } ?: "运行状态未知"
        }

        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = assessment.description,
                fontSize = 36.sp,
                fontWeight = FontWeight.Thin,
                color = assessment.color
            )
            Spacer(modifier = Modifier.height(30.dp))
            if (isConnected) {
                Text(
                    text = "设备运行正常",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Thin,
                    color = BlackGray
                )
                Text(
                    text = uptimeText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                    color = Gray
                )
            } else {
                Text(
                    text = "设备未连接",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Thin,
                    color = OrangeRed
                )
                Text(
                    text = "运行状态未知",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                    color = Gray
                )
            }
        }
    }

    fun formatUptime(millis: Long): String {
        val seconds = millis / 1000
        return when {
            seconds < 60 -> "刚刚启动"
            seconds < 3600 -> "已持续运行 ${seconds / 60} 分钟"
            seconds < 86400 -> "已持续运行 ${seconds / 3600} 小时"
            else -> {
                val days = seconds / 86400
                val hours = (seconds % 86400) / 3600
                "已持续运行 $days 天 $hours 小时"
            }
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
                        .background(data.status.color.withAlpha(0.2f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = data.status.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = data.status.color
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
    fun ControlArea(
        isConnected: Boolean,
        autoModeEnabled: Boolean,
        pumpState: ActuatorState,
        fanState: ActuatorState,
        lightState: ActuatorState,
        pumpLoading: Boolean,
        fanLoading: Boolean,
        lightLoading: Boolean,
        onAutoModeToggle: (Boolean) -> Unit,
        onPumpToggle: (Boolean) -> Unit,
        onFanToggle: (Boolean) -> Unit,
        onLightToggle: (Boolean) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "设备控制",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackGray
                )
                Text(
                    text = "仅在非智能模式下可进行手动控制",
                    fontSize = 12.sp,
                    color = Gray
                )
            }

            ControlCard(
                icon = R.drawable.ic_ai,
                iconColor = LightGreen,
                title = "智能模式",
                description = "根据环境自动控制",
                status = autoModeEnabled,
                enabled = isConnected,
                onToggle = onAutoModeToggle
            )

            ControlCard(
                icon = R.drawable.ic_fan,
                iconColor = SkyBlue,
                title = "风扇",
                description = "通风系统",
                status = fanState == ActuatorState.ON,
                enabled = isConnected && !autoModeEnabled && !fanLoading,
                onToggle = onFanToggle
            )

            ControlCard(
                icon = R.drawable.ic_bulb,
                iconColor = Yellow,
                title = "补光灯",
                description = "照明系统",
                status = lightState == ActuatorState.ON,
                enabled = isConnected && !autoModeEnabled && !lightLoading,
                onToggle = onLightToggle
            )

            ControlCard(
                icon = R.drawable.ic_pump,
                iconColor = OrangeYellow,
                title = "水泵",
                description = "灌溉系统",
                status = pumpState == ActuatorState.ON,
                enabled = isConnected && !autoModeEnabled && !pumpLoading,
                onToggle = onPumpToggle
            )
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
        onToggle: (Boolean) -> Unit
    ) {
        MicaCard(
            modifier = modifier.fillMaxWidth(),
            padding = XPadding.all(15),
            horizontalAlignment = Alignment.Start,
            onClick = { }
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
                    onCheckedChange = onToggle,
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
    fun ThresholdArea(
        viewModel: DeviceDetailViewModel,
        draftParams: ControlParams,
        isParamsChanged: Boolean
    ) {
        /*var moistureMinThreshold by remember { mutableIntStateOf(30) }
        var moistureMaxThreshold by remember { mutableIntStateOf(70) }
        var temperatureMinThreshold by remember { mutableIntStateOf(20) }
        var temperatureMaxThreshold by remember { mutableIntStateOf(30) }
        var lightIntensityMinThreshold by remember { mutableStateOf("500") }
        var lightIntensityMaxThreshold by remember { mutableStateOf("800") }
        var pumpMinIntervalThreshold by remember { mutableStateOf("300") }
        var pumpMaxDurationThreshold by remember { mutableStateOf("20") }*/
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "阈值设置",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackGray
                )
                if (isParamsChanged) {
                    Text(
                        text = "有未保存的更改",
                        fontSize = 12.sp,
                        color = OrangeRed
                    )
                } else {
                    Text(
                        text = "不需要的控制可设为极值",
                        fontSize = 12.sp,
                        color = Gray
                    )
                }
            }

            ThresholdCard(
                title = "温度下限",
                description = "低于该温度关闭风扇",
                value = draftParams.temperatureLow.toInt(),
                minValue = 0,
                maxValue = 50,
                unit = "℃",
                onValueChange = { newValue ->
                    viewModel.updateDraftParams(
                        draftParams.copy(temperatureLow = newValue.toFloat())
                    )
                }
            )

            ThresholdCard(
                title = "温度上限",
                description = "高于该温度开启风扇",
                value = draftParams.temperatureHigh.toInt(),
                minValue = 0,
                maxValue = 50,
                unit = "℃",
                onValueChange = { newValue ->
                    viewModel.updateDraftParams(
                        draftParams.copy(temperatureHigh = newValue.toFloat())
                    )
                }
            )

            ThresholdCard(
                title = "土壤湿度下限",
                description = "低于该湿度开启水泵",
                value = draftParams.soilMoistureLow.toInt(),
                minValue = 0,
                maxValue = 100,
                unit = "%",
                onValueChange = { newValue ->
                    viewModel.updateDraftParams(
                        draftParams.copy(soilMoistureLow = newValue.toFloat())
                    )
                }
            )

            ThresholdCard(
                title = "土壤湿度上限",
                description = "高于该湿度关闭水泵",
                value = draftParams.soilMoistureHigh.toInt(),
                minValue = 0,
                maxValue = 100,
                unit = "%",
                onValueChange = { newValue ->
                    viewModel.updateDraftParams(
                        draftParams.copy(soilMoistureHigh = newValue.toFloat())
                    )
                }
            )

            ThresholdCard(
                title = "光照强度下限",
                description = "低于该强度开启补光灯",
                value = draftParams.lightIntensityLow.toInt().toString(),
                minValue = 0,
                maxValue = 50000,
                unit = "Lux",
                onValueChange = { newValue: String ->
                    if (newValue.isEmpty()) {
                        // 用户清空输入框，设为 0
                        viewModel.updateDraftParams(
                            draftParams.copy(lightIntensityLow = 0f)
                        )
                    } else {
                        newValue.toIntOrNull()?.let { intValue ->
                            viewModel.updateDraftParams(
                                draftParams.copy(lightIntensityLow = intValue.toFloat())
                            )
                        }
                    }
                }
            )

            ThresholdCard(
                title = "光照强度上限",
                description = "高于该强度关闭水泵关闭补光灯",
                value = draftParams.lightIntensityHigh.toInt().toString(),
                minValue = 0,
                maxValue = 50000,
                unit = "Lux",
                onValueChange = { newValue: String ->
                    if (newValue.isEmpty()) {
                        // 用户清空输入框，设为 0
                        viewModel.updateDraftParams(
                            draftParams.copy(lightIntensityHigh = 0f)
                        )
                    } else {
                        newValue.toIntOrNull()?.let { intValue ->
                            viewModel.updateDraftParams(
                                draftParams.copy(lightIntensityHigh = intValue.toFloat())
                            )
                        }
                    }
                }
            )

            ThresholdCard(
                title = "水泵开启最小间隔时间",
                description = "短于该时间无法开启水泵",
                value = draftParams.minPumpInterval.toString(),
                minValue = 300,
                maxValue = 3600 * 24,
                unit = "秒",
                onValueChange = { newValue: String ->
                    if (newValue.isEmpty()) {
                        // 用户清空输入框，设为 0
                        viewModel.updateDraftParams(
                            draftParams.copy(minPumpInterval = 0)
                        )
                    } else {
                        newValue.toIntOrNull()?.let { intValue ->
                            viewModel.updateDraftParams(
                                draftParams.copy(minPumpInterval = intValue.toLong())
                            )
                        }
                    }
                }
            )

            ThresholdCard(
                title = "水泵开启最大持续时间",
                description = "超过该时间水泵自动关闭",
                value = draftParams.maxPumpDuration.toString(),
                minValue = 5,
                maxValue = 3600,
                unit = "秒",
                onValueChange = { newValue: String ->
                    if (newValue.isEmpty()) {
                        // 用户清空输入框，设为 0
                        viewModel.updateDraftParams(
                            draftParams.copy(maxPumpDuration = 0)
                        )
                    } else {
                        newValue.toIntOrNull()?.let { intValue ->
                            viewModel.updateDraftParams(
                                draftParams.copy(maxPumpDuration = intValue.toLong())
                            )
                        }
                    }
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
        scalingFactor: Float = 1f,
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
                        text = "${(value * scalingFactor).toInt()}$unit",
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

    @Composable
    fun ThresholdCard(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
        value: String,
        minValue: Int = 0,
        maxValue: Int = 100,
        unit: String,
        onValueChange: (String) -> Unit
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

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    AliveTextField(
                        modifier = Modifier.weight(1f),
                        label = "$minValue $unit ~ $maxValue $unit",
                        placeholder = "请输入阈值",
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = value,
                        onValueChange = { newValue ->
                            // 只保留数字字符
                            val filtered = newValue.filter { it.isDigit() }
                            // 只有当过滤后的字符串与原始输入不同时才触发更新
                            // 避免无限循环，同时保证只输入数字
                            if (filtered != newValue) {
                                onValueChange(filtered)
                            } else {
                                onValueChange(newValue)
                            }
                        }
                    )
                    Text(
                        text = unit,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen,
                        maxLines = 1
                    )
                }
            }
        }
    }

    @Composable
    fun CalibrationArea(
        viewModel: DeviceDetailViewModel,
        isConnected: Boolean
    ) {
        var showSoilWizard by remember { mutableStateOf(false) }
        var showLightWizard by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "传感器校准",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackGray
                )

                Text(
                    text = "配对后建议立即进行一次校准",
                    fontSize = 12.sp,
                    color = Gray
                )
            }

            /*CalibrationCard(
                iconColor = SkyBlue,
                title = "校准温湿度",
                description = "将进行温湿度传感器校准流程"
            ) {

            }*/

            CalibrationCard(
                iconColor = Yellow,
                title = "光照强度校准",
                description = "将进行光照传感器校准流程",
                onClick = {
                    if (!isConnected) {
                        Toast.makeText(this@DeviceDetailsActivity, "设备未连接", Toast.LENGTH_SHORT)
                            .show()
                        return@CalibrationCard
                    }
                    showLightWizard = true
                }
            )

            CalibrationCard(
                iconColor = OrangeYellow,
                title = "土壤湿度校准",
                description = "将进行土壤湿度传感器校准流程",
                onClick = {
                    if (!isConnected) {
                        Toast.makeText(this@DeviceDetailsActivity, "设备未连接", Toast.LENGTH_SHORT)
                            .show()
                        return@CalibrationCard
                    }
                    showSoilWizard = true
                }
            )

            // 光照校准向导
            if (showLightWizard) {
                CalibrationWizardDialog(
                    title = "光照强度校准",
                    steps = listOf(
                        CalibrationStep(
                            title = "遮光校准",
                            instruction = "请将传感器完全遮光或置于完全黑暗的环境，确保无光线进入",
                            type = CalibrationType.LIGHT_MIN
                        ),
                        CalibrationStep(
                            title = "强光校准",
                            instruction = "请将传感器置于强光下（建议在直射的太阳光下），此步可跳过",
                            type = CalibrationType.LIGHT_MAX
                        )
                    ),
                    onDismiss = { showLightWizard = false },
                    onComplete = {
                        showLightWizard = false
                        Toast.makeText(
                            this@DeviceDetailsActivity,
                            "光照校准完成",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    viewModel = viewModel
                )
            }

            // 土壤湿度校准向导
            if (showSoilWizard) {
                CalibrationWizardDialog(
                    title = "土壤湿度校准",
                    steps = listOf(
                        CalibrationStep(
                            title = "干态校准",
                            instruction = "请将土壤湿度传感器置于干燥空气中，确保表面无水",
                            type = CalibrationType.SOIL_DRY
                        ),
                        CalibrationStep(
                            title = "湿态校准",
                            instruction = "请将传感器探头完全浸入水中，等待稳定",
                            type = CalibrationType.SOIL_WET
                        )
                    ),
                    onDismiss = { showSoilWizard = false },
                    onComplete = {
                        showSoilWizard = false
                        Toast.makeText(
                            this@DeviceDetailsActivity,
                            "土壤湿度校准完成",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    viewModel = viewModel
                )
            }
        }
    }

    @Composable
    fun CalibrationCard(
        modifier: Modifier = Modifier,
        iconColor: Color,
        title: String,
        description: String,
        onClick: () -> Unit
    ) {
        MicaCard(
            modifier = modifier.fillMaxWidth(),
            padding = XPadding.all(15),
            horizontalAlignment = Alignment.Start,
            onClick = onClick
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
                            painter = painterResource(R.drawable.ic_construct),
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

                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Gray
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalibrationWizardDialog(
        title: String,
        steps: List<CalibrationStep>,
        onDismiss: () -> Unit,
        onComplete: () -> Unit,
        viewModel: DeviceDetailViewModel
    ) {
        var currentStepIndex by remember { mutableIntStateOf(0) }
        val calibrationLoading by viewModel.calibrationLoading.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.calibrationEvents.collect { event ->
                if (currentStepIndex < steps.size && event.type == steps[currentStepIndex].type) {
                    if (event.success) {
                        if (currentStepIndex < steps.size - 1) {
                            currentStepIndex++
                        } else {
                            onComplete()
                        }
                    } else {
                        Toast.makeText(
                            this@DeviceDetailsActivity,
                            "校准失败，请重试",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        XDialog.Empty(
            backgroundColor = Color.White,
            padding = XPadding.all(25),
            borderRadius = 25,
            onDismiss = onDismiss
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // 标题
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackGray
                )

                Spacer(modifier = Modifier.height(15.dp))

                // 步骤标题
                Text(
                    text = "步骤 ${currentStepIndex + 1}/${steps.size}：${steps[currentStepIndex].title}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGreen
                )

                Spacer(modifier = Modifier.height(5.dp))

                // 详细操作指引
                Text(
                    text = steps[currentStepIndex].instruction,
                    fontSize = 16.sp,
                    color = BlackGray
                )

                Spacer(modifier = Modifier.height(15.dp))

                // 提示文字
                Text(
                    text = "请按照指引操作，然后点击“开始校准”",
                    fontSize = 12.sp,
                    color = Gray
                )

                Spacer(modifier = Modifier.height(15.dp))

                // 按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (calibrationLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = LightGreen
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }

                    XItem.Button(
                        text = "取消",
                        color = XColorGroup(
                            background = OrangeRed,
                            content = Color.White
                        ),
                        onClick = onDismiss,
                        enabled = !calibrationLoading
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    XItem.Button(
                        text = "开始校准",
                        color = XColorGroup(
                            background = LightGreen,
                            content = Color.White
                        ),
                        onClick = { viewModel.calibrate(steps[currentStepIndex].type) },
                        enabled = !calibrationLoading
                    )
                }
            }
        }
    }
}