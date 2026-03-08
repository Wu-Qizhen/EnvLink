package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XIcon
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.DeviceAddActivity
import com.codeintellix.envlink.activity.common.DeviceDetailsActivity
import com.codeintellix.envlink.activity.common.widget.AliveTextField
import com.codeintellix.envlink.activity.common.widget.FadeEdge
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.common.widget.ScrollableRowTab
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.data.device.DeviceListViewModel
import com.codeintellix.envlink.data.device.DeviceListViewModelFactory
import com.codeintellix.envlink.data.house.HouseNameManager
import com.codeintellix.envlink.data.weather.WeatherViewModel
import com.codeintellix.envlink.entity.device.Device
import com.codeintellix.envlink.entity.house.RoomType
import com.codeintellix.envlink.entity.sensor.SensorData
import com.codeintellix.envlink.entity.weather.WeatherState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EnvLinkPage() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val deviceListViewModel: DeviceListViewModel = viewModel(
        factory = DeviceListViewModelFactory(context)
    )
    val devices by deviceListViewModel.devices.collectAsState()

    // 权限状态
    val permissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 首次加载标记，避免重复请求
    var isFirstLoad by remember { mutableStateOf(true) }

    // 当权限状态变化或首次加载时触发天气获取
    LaunchedEffect(permissionState.status, isFirstLoad) {
        if (permissionState.status.isGranted && isFirstLoad) {
            weatherViewModel.fetchWeather(context)
            isFirstLoad = false
        } else if (!permissionState.status.isGranted && isFirstLoad) {
            permissionState.launchPermissionRequest()
        }
    }

    // 拖拽回弹效果
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(IntOffset(0, 0), IntOffset.VectorConverter) }

    val tabs = listOf("全屋") + RoomType.getAllNames()
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            XHeader.IconText(
                icon = R.drawable.logo_env_link_green,
                text = stringResource(R.string.my_device),
                textColor = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 28,
                headerPadding = XPadding.all(20),
                iconTextPadding = XPadding.all(10),
                modifier = Modifier.clickVfx(
                    onClick = {
                        Toast.makeText(context, "🌱", Toast.LENGTH_SHORT).show()
                    }
                )
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
                        top = 20.dp,
                        bottom = 120.dp
                    )
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                HeaderArea(
                    context = context,
                    weatherState = weatherState,
                    onRefresh = {
                        // 点击刷新时，如果权限未授予，先请求权限；否则直接刷新
                        if (permissionState.status.isGranted) {
                            weatherViewModel.refreshWeather(context)
                        } else {
                            permissionState.launchPermissionRequest()
                        }
                    }
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    ScrollableRowTab(
                        tabs = tabs,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart),
                        padding = XPadding.only(start = 20, end = 80),
                        fadeEdge = FadeEdge.Both(
                            leftEndRatio = 0.05f,
                            rightStartRatio = 0.75f,
                            rightEndRatio = 0.85f
                        )
                    )

                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            tint = BlackGray,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                DevicesArea(devices = devices)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 20.dp, vertical = 15.dp)
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
            XIcon.RoundPlane(
                icon = R.drawable.ic_add,
                iconSize = 20,
                color = XColorGroup(
                    background = LightGreen,
                    content = Color.White
                ),
                planeSize = 40,
                onClick = {
                    context.startActivity(Intent(context, DeviceAddActivity::class.java))
                }
            )
        }
    }
}

/**
 * 头部区域
 */
@Composable
fun HeaderArea(
    context: Context,
    weatherState: WeatherState,
    onRefresh: () -> Unit
) {
    val houseNameManager = remember { HouseNameManager(context) }
    var houseName by remember { mutableStateOf(houseNameManager.getHouseName()) }
    var isEditing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            // 编辑模式：显示文本框
            AliveTextField(
                label = "房屋名称",
                placeholder = "请输入房屋名称",
                value = houseName,
                focusRequester = focusRequester,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (houseName.trim().isBlank()) {
                        houseName = HouseNameManager.DEFAULT_HOUSE_NAME
                    }
                    // 保存到 SharedPreferences 并退出编辑
                    houseNameManager.saveHouseName(houseName.trim())
                    isEditing = false
                }),
            ) { houseName = it }
            // 自动获取焦点
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Text(
                text = houseName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlackGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clickVfx(
                        onClick = {
                            Toast.makeText(context, "长按编辑", Toast.LENGTH_SHORT).show()
                        },
                        onLongClick = { isEditing = true }
                    )
                    .basicMarquee()
            )
            // 天气显示区域
            when (weatherState) {
                is WeatherState.Loading -> {
                    // 加载中显示进度条
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = LightGreen,
                        strokeWidth = 4.dp
                    )
                }

                is WeatherState.Success -> {
                    Row(
                        modifier = Modifier.clickVfx(onClick = { onRefresh() }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = weatherState.weather.icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = weatherState.temperature,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlackGray
                        )
                    }
                }

                is WeatherState.Error -> {
                    // 错误时显示默认图标和简短提示，点击可重试
                    Row(
                        modifier = Modifier.clickVfx(onClick = { onRefresh() }),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.weather_cloudy), // 备选图标
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "--°",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlackGray
                        )
                    }
                }
            }
        }
    }
}

/**
 * 设备区域
 */
@Composable
fun DevicesArea(devices: List<Device>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        /*DeviceCard(
            name = "微环境控制器",
            location = "阳台",
            isOnline = true,
        )

        DeviceCard(
            name = "微环境控制器",
            location = "窗台",
            isOnline = false
        )*/

        if (devices.isEmpty()) {
            // 空状态提示
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Image(
                        painter = painterResource(id = R.drawable.illustration_plant_failed), // 可使用现有图标
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(0.4f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "暂无设备，点击右上角添加",
                        fontSize = 16.sp,
                        color = Gray
                    )
                }
            }
        } else {
            devices.forEach { device ->
                DeviceCard(
                    device = device,
                    isOnline = true, // 暂时固定在线
                )
            }
        }
    }
}

/**
 * 设备卡片
 */
/*
@Composable
fun DeviceCard(
    name: String,
    location: String,
    isOnline: Boolean,
    temperature: Int = 26,
    humidity: Int = 70,
    light: Int = 850,
    moisture: Int = 45
) {
    val context = LocalContext.current
    MicaCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        padding = XPadding.all(10),
        onClick = {
            val intent = Intent(context, DeviceDetailsActivity::class.java)
            context.startActivity(intent)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_plant),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOnline) BlackGray else Gray
                    )
                    Text(
                        text = "$location | ${if (isOnline) "在线" else "离线"}",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EnvironmentIndicator(
                    // modifier = Modifier.weight(1f),
                    label = "温度",
                    value = if (isOnline) temperature.toString() else "--",
                    unit = "℃",
                    isOnline = isOnline
                )
                EnvironmentIndicator(
                    // modifier = Modifier.weight(1f),
                    label = "湿度",
                    value = if (isOnline) humidity.toString() else "--",
                    unit = "%",
                    isOnline = isOnline
                )
                EnvironmentIndicator(
                    // modifier = Modifier.weight(1f),
                    label = "光照",
                    value = if (isOnline) light.toString() else "--",
                    unit = "Lux",
                    isOnline = isOnline
                )
                EnvironmentIndicator(
                    // modifier = Modifier.weight(1f),
                    label = "土壤",
                    value = if (isOnline) moisture.toString() else "--",
                    unit = "%",
                    isOnline = isOnline
                )
            }
        }
    }
}
*/

@Composable
fun DeviceCard(
    device: Device,
    isOnline: Boolean
) {
    val context = LocalContext.current
    val sensorData = remember(device.latestSensorData) {
        if (device.latestSensorData.isNotEmpty()) {
            try {
                val gson = Gson()
                gson.fromJson(device.latestSensorData, SensorData::class.java)
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    MicaCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        padding = XPadding.all(10),
        onClick = {
            val intent = Intent(context, DeviceDetailsActivity::class.java).apply {
                putExtra("device_address", device.address)
            }
            context.startActivity(intent)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_plant),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = device.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOnline) BlackGray else Gray
                    )
                    Text(
                        text = "${device.room} | ${if (isOnline) "在线" else "离线"}",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (sensorData != null) {
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "温度",
                        value = sensorData.temperature.toInt().toString(),
                        unit = "℃",
                        isOnline = isOnline
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "湿度",
                        value = sensorData.humidity.toInt().toString(),
                        unit = "%",
                        isOnline = isOnline
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "光照",
                        value = sensorData.lightIntensity.toString(),
                        unit = "Lux",
                        isOnline = isOnline
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "土壤",
                        value = sensorData.soilMoisture.toInt().toString(),
                        unit = "%",
                        isOnline = isOnline
                    )
                } else {
                    EnvironmentIndicator(
                        label = "温度",
                        value = "--",
                        unit = "℃",
                        isOnline = false
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "湿度",
                        value = "--",
                        unit = "%",
                        isOnline = false
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "光照",
                        value = "--",
                        unit = "Lux",
                        isOnline = false
                    )
                    EnvironmentIndicator(
                        // modifier = Modifier.weight(1f),
                        label = "土壤",
                        value = "--",
                        unit = "%",
                        isOnline = false
                    )
                }
            }
        }
    }
}

/**
 * 环境指标
 */
@Composable
fun EnvironmentIndicator(
    // modifier: Modifier = Modifier,
    label: String,
    value: String,
    unit: String,
    isOnline: Boolean = true
) {
    Row(
        // modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOnline) BlackGray else Gray
        )
        Spacer(modifier = Modifier.width(3.dp))
        Column {
            Text(
                text = unit,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isOnline) BlackGray else Gray
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Gray
            )
        }
    }
}