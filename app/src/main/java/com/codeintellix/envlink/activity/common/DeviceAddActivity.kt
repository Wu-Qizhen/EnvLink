package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XHeader
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.theme.Blue
import com.codeintellix.envlink.activity.theme.GreenWhite
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
        val scrollState = rememberScrollState()
        val isScrolled by remember {
            derivedStateOf { scrollState.value > 0 }
        }

        val scanResults by viewModel.scanResults.collectAsState()
        val isScanning by viewModel.isScanning.collectAsState()

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
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasPermissions = permissions.values.all { it }
            if (hasPermissions) {
                viewModel.startScan()
            } else {
                Toast.makeText(context, "需要权限才能扫描蓝牙设备", Toast.LENGTH_SHORT).show()
            }
        }

        // 检查权限状态
        LaunchedEffect(Unit) {
            val allGranted = permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            if (allGranted) {
                hasPermissions = true
                viewModel.startScan()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }

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
                    .alpha(0.6f)
                    .background(
                        shape = CircleShape, brush = Brush.radialGradient(
                            listOf(
                                Blue,
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
                    if (!hasPermissions) {
                        Text("正在请求权限...")
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Button(
                                onClick = { viewModel.startScan() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                enabled = !isScanning
                            ) {
                                Text(if (isScanning) "扫描中..." else "重新扫描")
                            }
                            if (isScanning) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            }
                            LazyColumn {
                                items(scanResults) { device ->
                                    DeviceItem(
                                        device = device,
                                        onAddClick = {
                                            viewModel.addDevice(device)
                                            Toast.makeText(
                                                context,
                                                "已添加 ${device.name}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun DeviceItem(device: BluetoothDevice, onAddClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = device.name ?: "未知设备", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = device.address, modifier = Modifier.padding(bottom = 8.dp))
            Button(onClick = onAddClick) {
                Text("添加")
            }
        }
    }
}