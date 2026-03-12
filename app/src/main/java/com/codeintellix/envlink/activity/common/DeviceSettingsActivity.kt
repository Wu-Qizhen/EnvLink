package com.codeintellix.envlink.activity.common

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XDialog
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XItem
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
        val deviceSystemVersion =
            intent.getStringExtra(ActivityExtra.DEVICE_SYSTEM_VERSION_EXTRA) ?: "未知"

        enableEdgeToEdge()
        setContent {
            XBackground.Gradient(
                backgroundColors = listOf(GreenWhite, Color.White, GreenWhite),
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                DeviceSettingsScreen(
                    deviceAddress = deviceAddress,
                    deviceSystemVersion = deviceSystemVersion
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceSettingsScreen(
        deviceAddress: String,
        deviceSystemVersion: String
    ) {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val scrollState = rememberScrollState()
        val isScrolled by remember {
            derivedStateOf { scrollState.value > 0 }
        }

        val scope = androidx.compose.runtime.rememberCoroutineScope()

        val viewModel: DeviceDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
            factory = DeviceDetailViewModelFactory(application, deviceAddress)
        )
        val device by viewModel.device.collectAsState()

        var deviceName by remember { mutableStateOf(device?.name ?: "") }
        var isEditingName by remember { mutableStateOf(false) }

        // 删除设备按钮
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
                    onClick = { isEditingName = true }
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

                if (isEditingName) {
                    XDialog.Empty(
                        backgroundColor = Color.White,
                        padding = XPadding.all(25),
                        borderRadius = 25,
                        onDismiss = { isEditingName = false }
                    ) {
                        Text(
                            text = "设备名称",
                            fontSize = 16.sp,
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
                                    isEditingName = false
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
                                            isEditingName = false
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
                        label = "创建时间",
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
                        value = "V$deviceSystemVersion"
                    )
                }
                AcrylicButton(
                    text = "删除设备",
                    backgroundColor = OrangeRed
                ) {
                    showDeleteDialog = true
                }

                if (showDeleteDialog) {
                    XDialog.Confirm(
                        padding = XPadding.all(25),
                        borderRadius = 25,
                        backgroundColor = Color.White,
                        dismissButtonColor = LightGreen,
                        confirmButtonColor = OrangeRed,
                        title = "删除确认",
                        message = "确定要删除此设备吗？删除后可重新配对添加",
                        dismiss = "取消",
                        confirm = "删除",
                        onDismiss = { showDeleteDialog = false },
                        onConfirm = {
                            scope.launch {
                                // TODO
                                DeviceRepository.getInstance(applicationContext)
                                    .removeDevice(deviceAddress)
                            }
                            showDeleteDialog = false
                        }
                    )
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
            java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(date)
    }
}