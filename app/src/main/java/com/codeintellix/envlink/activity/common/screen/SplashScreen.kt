package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.typography.XFont
import aethex.matrix.ui.XBackground
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.theme.Green
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.PaleGreen

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@Composable
fun SplashScreen() {
    /*val isContentVisible = remember { mutableStateOf(false) }

    // 启动屏幕内容淡入动画
    LaunchedEffect(key1 = true) {
        isContentVisible.value = true
    }

    AnimatedVisibility(
        visible = isContentVisible.value,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    ) {*/
    XBackground.Space(
        backgroundColor = Color.White,
        activeColor = PaleGreen.withAlpha(0.6f),
        secondaryColor = Green,
        tertiaryColor = LightGreen.withAlpha(0.5f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(
                    Alignment.Center
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 显示应用图标
                Image(
                    painter = painterResource(id = R.drawable.logo_env_link_green),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.fillMaxWidth(0.4f)
                )

                Spacer(modifier = Modifier.size(20.dp))

                // 显示应用名
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 32.sp,
                    fontFamily = XFont.THEME,
                    color = Color.Black
                )
            }

            // 显示 Slogan
            Text(
                text = stringResource(id = R.string.app_slogan),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                fontSize = 20.sp,
                fontFamily = XFont.THEME,
                color = Color.Black
            )
        }
    }
    /*}*/
}

@Preview
@Composable
private fun SplashScreenPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SplashScreen()
    }
}