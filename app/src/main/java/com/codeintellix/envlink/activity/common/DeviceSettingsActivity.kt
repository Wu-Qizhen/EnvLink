package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XDialog
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XItem
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeintellix.envlink.MainActivity
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.widget.AcrylicButton
import com.codeintellix.envlink.activity.common.widget.AliveTextField
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.GreenWhite
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.WhiteGray
import com.codeintellix.envlink.data.device.DeviceDetailViewModel
import com.codeintellix.envlink.data.device.DeviceDetailViewModelFactory
import com.codeintellix.envlink.data.device.DeviceRepository
import com.codeintellix.envlink.entity.cosnt.ActivityExtra
import kotlinx.coroutines.launch

class DeviceSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deviceAddress = intent.getStringExtra(ActivityExtra.DEVICE_ADDRESS_EXTRA) ?: ""

        enableEdgeToEdge()
        setContent {
            XBackground.Gradient(
                backgroundColors = listOf(GreenWhite, Color.White, GreenWhite),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceSettingsScreen(
                    deviceAddress = deviceAddress
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceSettingsScreen(
        deviceAddress: String
    ) {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val scrollState = rememberScrollState()
        val isScrolled by remember {
            derivedStateOf { scrollState.value > 0 }
        }

        val scope = rememberCoroutineScope()
        val viewModel: DeviceDetailViewModel = viewModel(
            factory = DeviceDetailViewModelFactory(application, deviceAddress)
        )
        val device by viewModel.device.collectAsState()
        var deviceName by remember { mutableStateOf(device?.name ?: "") }
        var showEditDialog by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(systemBarPadding)
        ) {
            XHeader.BackText(
                text = "设置",
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
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 120.dp
                    )
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // 设备名称设置
                MicaCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        deviceName = device?.name ?: ""
                        showEditDialog = true
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "设备名称",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            color = BlackGray
                        )
                        Text(
                            text = device?.name
                                ?: stringResource(R.string.microenvironment_controller),
                            fontSize = 16.sp,
                            color = Gray
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = Gray
                        )
                    }
                }

                if (showEditDialog) {
                    XDialog.Empty(
                        backgroundColor = Color.White,
                        padding = XPadding.all(25),
                        borderRadius = 25,
                        onDismiss = { showEditDialog = false }
                    ) {
                        Text(
                            text = "设备名称",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackGray
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        AliveTextField(
                            label = "限制 30 个字符",
                            maxLines = 1,
                            placeholder = "请输入设备名称",
                            modifier = Modifier.fillMaxWidth(),
                            value = deviceName,
                            onValueChange = { deviceName = it },
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            XItem.Button(
                                text = "取消",
                                color = XColorGroup(
                                    background = OrangeRed,
                                    content = Color.White
                                ),
                                onClick = {
                                    showEditDialog = false
                                    deviceName = device?.name ?: ""
                                },
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            XItem.Button(
                                text = "保存",
                                color = XColorGroup(
                                    background = LightGreen,
                                    content = Color.White
                                ),
                                onClick = {
                                    device?.let {
                                        val updatedDevice = it.copy(name = deviceName.take(30))
                                        scope.launch {
                                            DeviceRepository.getInstance(applicationContext)
                                                .updateDevice(updatedDevice)
                                            Toast.makeText(
                                                this@DeviceSettingsActivity,
                                                "设备名称修改成功",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            showEditDialog = false
                                        }
                                    }
                                },
                            )
                        }
                    }
                }

                // 设备信息
                MicaCard(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "设备信息",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoRow(label = "设备地址", value = device?.address ?: "未知")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoRow(
                        label = "设备添加时间",
                        value = device?.createTime?.let { formatTime(it) } ?: "未知")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoRow(
                        label = "最后连接时间",
                        value = device?.lastConnectedTime?.let { formatTime(it) } ?: "未知")
                }

                // 系统版本信息
                MicaCard(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "系统信息",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoRow(
                        label = "系统版本",
                        value = device?.let { "Version ${it.firmwareVersion}" } ?: "未知"
                    )
                }
                AcrylicButton(
                    text = "删除设备",
                    backgroundColor = OrangeRed
                ) {
                    showDeleteDialog = true
                }

                if (showDeleteDialog) {
                    XDialog.Empty(
                        backgroundColor = Color.White,
                        padding = XPadding.all(25),
                        borderRadius = 25,
                        onDismiss = { showDeleteDialog = false }
                    ) {
                        // 标题
                        Text(
                            text = "删除确认",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // 详细操作指引
                        Text(
                            text = "确定要删除此设备吗？删除后可重新配对添加",
                            fontSize = 16.sp,
                            color = BlackGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // 按钮行
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            XItem.Button(
                                text = "取消",
                                color = XColorGroup(
                                    background = LightGreen,
                                    content = Color.White
                                ),
                                onClick = { showDeleteDialog = false }
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            XItem.Button(
                                text = "删除",
                                color = XColorGroup(
                                    background = OrangeRed,
                                    content = Color.White
                                ),
                                onClick = {
                                    scope.launch {
                                        // 删除设备
                                        DeviceRepository.getInstance(applicationContext)
                                            .removeDevice(deviceAddress)

                                        Toast.makeText(
                                            this@DeviceSettingsActivity,
                                            "设备删除成功",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // 跳转到主页面并清除活动栈
                                        val intent =
                                            Intent(
                                                this@DeviceSettingsActivity,
                                                MainActivity::class.java
                                            )
                                        intent.putExtra(
                                            ActivityExtra.SHOW_SPLASH_SCREEN_EXTRA,
                                            false
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    showDeleteDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun InfoRow(label: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                maxLines = 1,
                fontSize = 14.sp,
                color = BlackGray
            )
            Text(
                text = value,
                maxLines = 1,
                fontSize = 14.sp,
                color = Gray
            )
        }
    }

    fun formatTime(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val sdf =
            java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(date)
    }
}