package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XHeader
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.AboutActivity
import com.codeintellix.envlink.activity.common.widget.AcrylicButton
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.WhiteGray

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@Composable
fun MinePage() {
    val themeColor = LightGreen
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            XHeader.IconText(
                icon = R.drawable.ic_smile,
                iconColor = themeColor,
                text = R.string.mine,
                textColor = Color.Black,
                fontSize = 28,
                fontWeight = FontWeight.Normal,
                headerPadding = XPadding.all(20),
                iconTextPadding = XPadding.all(10)
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(150.dp))
                        Image(
                            painter = painterResource(id = R.drawable.illustration_plant_build), // 可使用现有图标
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "建造中",
                            fontSize = 16.sp,
                            color = Gray
                        )
                    }
                }

                /*XCard.Lively(
                    modifier = Modifier
                        .fillMaxWidth(),
                    borderRadius = 30,
                    color = XColorGroup(
                        background = LightGreen,
                        activeBackground = LightGreen.withAlpha(0.8f)
                    ),
                    padding = XPadding.horizontal(15).vertical(10),
                    onClick = {
                        context.startActivity(
                            Intent(context, DeviceTestActivity::class.java)
                        )
                    }
                ) {
                    Text(
                        text = "调试选项",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }*/
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter)
        ) {
            AcrylicButton(
                text = "关于"
            ) {
                context.startActivity(
                    Intent(context, AboutActivity::class.java)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "${stringResource(id = R.string.copyrights)}\n${stringResource(id = R.string.all_rights_reserved)}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickVfx()
                    .fillMaxWidth(),
                fontSize = 12.sp,
                color = Gray
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}