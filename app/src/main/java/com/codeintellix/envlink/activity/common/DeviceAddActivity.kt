package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XCard
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XItem
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.GreenWhite
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.data.device.DeviceViewModel
import com.codeintellix.envlink.data.device.DeviceViewModelFactory

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class DeviceAddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XBackground.Gradient(
                backgroundColors = listOf(GreenWhite, Color.White, GreenWhite),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceAddScreen()
            }
        }
    }

    @Composable
    fun DeviceAddScreen(
        viewModel: DeviceViewModel = viewModel(
            factory = DeviceViewModelFactory(LocalContext.current)
        )
    ) {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val context = LocalContext.current
        val listState = rememberLazyListState()
        val isScrolled by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
        }

        // 权限请求
        var hasPermissions by remember { mutableStateOf(false) }
        val permissionsToRequest =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasPermissions = permissions.values.all { it }
            if (hasPermissions) {
                viewModel.startBleScan()
            } else {
                Toast.makeText(context, "需要权限才能连接设备", Toast.LENGTH_SHORT).show()
            }
        }

        // 检查权限状态
        LaunchedEffect(Unit) {
            val allGranted = permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            if (allGranted) {
                hasPermissions = true
                viewModel.startBleScan()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }

        // 扫描结果
        val isScanning by viewModel.isScanning.collectAsState()
        val scanResults by viewModel.scanResults.collectAsState()
        val connectionState by viewModel.connectionState.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-100).dp, y = (-100).dp)
                    .size(300.dp)
                    // .fillMaxWidth()
                    // .scale(scaleX = 1.2f, scaleY = 1.2f)
                    .aspectRatio(1f)
                    .alpha(0.4f)
                    .background(
                        shape = CircleShape, brush = Brush.radialGradient(
                            listOf(
                                LightGreen,
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
                    text = stringResource(R.string.add_device),
                    textColor = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28,
                    iconColor = Color.Black,
                    headerPadding = XPadding.all(20)
                )

                Canvas(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    drawLine(
                        color = if (isScrolled) WhiteGray else Color.Transparent,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f)
                    )
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            MicaCard(
                                modifier = Modifier.fillMaxWidth(),
                                padding = XPadding.vertical(20).horizontal(15),
                                shadowRadius = 10,
                                shadowAlpha = 0.15f
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.illustration_cabinet),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                if (hasPermissions) {
                                    Text(
                                        text = "设备搜索中",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlackGray
                                    )
                                    Text(
                                        text = "请将手机尽量靠近要添加的设备",
                                        fontSize = 14.sp,
                                        color = LightGreen
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
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
                                            if (isScanning) {
                                                viewModel.stopScan()
                                            } else {
                                                viewModel.startBleScan()
                                            }
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (isScanning) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(30.dp),
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                            }

                                            Text(
                                                text = if (isScanning) "搜索中" else "重新搜索",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "未开启权限",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OrangeRed
                                    )
                                    Text(
                                        text = "正在请求权限",
                                        fontSize = 14.sp,
                                        color = Gray
                                    )
                                }
                            }
                        }
                    }

                    if (hasPermissions) {
                        items(scanResults) { device ->
                            DeviceItem(
                                device = device,
                                onAddClick = {
                                    // TODO
                                    Toast.makeText(
                                        context,
                                        "开始配对",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.addDevice(device)
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun DeviceItem(
        device: BluetoothDevice,
        onAddClick: () -> Unit
    ) {
        MicaCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
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
                            text = device.name ?: "未知设备",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlackGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.basicMarquee()
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = device.address,
                            fontSize = 14.sp,
                            color = Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.basicMarquee()
                        )
                    }
                }

                XItem.Button(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    color = XColorGroup(
                        background = LightGreen,
                        activeBackground = LightGreen.withAlpha(0.8f),
                        border = null,
                        activeBorder = null,
                        content = Color.White,
                        activeContent = Color.White
                    ),
                    text = "添加",
                    onClick = onAddClick
                )
            }
        }
    }
}