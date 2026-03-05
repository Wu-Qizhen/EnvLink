package com.codeintellix.envlink.activity.prototype

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.data.device.DeviceViewModel
import com.codeintellix.envlink.data.device.DeviceViewModelFactory
import com.codeintellix.envlink.entity.device.ConnectionState

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class DeviceTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeviceTestScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceTestScreen(
        viewModel: DeviceViewModel = viewModel(factory = DeviceViewModelFactory(LocalContext.current))
    ) {
        val context = LocalContext.current
        val connectionState by viewModel.connectionState.collectAsState()
        val receivedData by viewModel.receivedData.collectAsState()

        // 权限管理
        var hasPermissions by remember { mutableStateOf(false) }
        val permissions =
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
        ) { granted ->
            hasPermissions = granted.values.all { it }
            if (!hasPermissions) {
                Toast.makeText(context, "需要蓝牙权限", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            val allGranted = permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            hasPermissions = allGranted
            if (!allGranted) {
                permissionLauncher.launch(permissions)
            }
        }

        // 用户输入的设备地址
        var addressInput by remember { mutableStateOf("") }
        val listState = rememberLazyListState()

        // 自动滚动到底部显示最新消息
        LaunchedEffect(receivedData) {
            listState.animateScrollToItem(Int.MAX_VALUE)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "蓝牙设备测试",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 连接状态显示
                Text(
                    text = "状态：$connectionState",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (connectionState !is ConnectionState.Connected) {
                    // 未连接时显示输入框和连接按钮
                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("设备 MAC 地址") },
                        placeholder = { Text("AA:BB:CC:DD:EE:FF") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (!hasPermissions) {
                                permissionLauncher.launch(permissions)
                                return@Button
                            }
                            if (addressInput.isBlank()) {
                                Toast.makeText(context, "请输入地址", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            try {
                                val bluetoothManager =
                                    context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
                                val adapter = bluetoothManager.adapter
                                val device = adapter.getRemoteDevice(addressInput)
                                viewModel.connectToDevice(device)
                            } catch (_: IllegalArgumentException) {
                                Toast.makeText(context, "无效的 MAC 地址", Toast.LENGTH_SHORT)
                                    .show()
                            } catch (_: SecurityException) {
                                Toast.makeText(context, "权限不足", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = hasPermissions,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("连接")
                    }
                } else {
                    // 已连接：显示发送区和接收日志
                    var sendText by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = sendText,
                        onValueChange = { sendText = it },
                        label = { Text("输入发送内容") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (sendText.isNotBlank()) {
                                viewModel.sendData(sendText)
                                sendText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("发送")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // 接收数据显示区（使用 LazyColumn 自动滚动）
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        reverseLayout = false,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(receivedData.lines()) { line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 断开按钮
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.disconnectDevice() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("断开连接")
                    }
                }
            }
        }
    }
}