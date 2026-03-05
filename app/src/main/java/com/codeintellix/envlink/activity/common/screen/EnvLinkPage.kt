package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XIcon
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
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
import com.codeintellix.envlink.activity.common.DeviceAddActivity
import com.codeintellix.envlink.activity.common.DeviceDetailsActivity
import com.codeintellix.envlink.activity.common.widget.FadeEdge
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.common.widget.ScrollableRowTab
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.WhiteGray
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@Composable
fun EnvLinkPage() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    // 拖拽回弹效果
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(IntOffset(0, 0), IntOffset.VectorConverter) }

    val tabs =
        listOf("全屋", "阳台", "客厅", "卧室", "厨房", "餐厅", "卫生间")
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
                HeaderArea()

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

                DevicesArea()
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
fun HeaderArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "碧桂园",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = BlackGray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.weather_cloudy),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "28°",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlackGray
            )
        }
    }
}

/**
 * 设备区域
 */
@Composable
fun DevicesArea() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        DeviceCard(
            name = "微环境控制器",
            location = "阳台",
            isOnline = true,
        )

        DeviceCard(
            name = "微环境控制器",
            location = "窗台",
            isOnline = false
        )
    }
}

/**
 * 设备卡片
 */
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