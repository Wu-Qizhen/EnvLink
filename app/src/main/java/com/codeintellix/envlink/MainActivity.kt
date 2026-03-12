package com.codeintellix.envlink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.codeintellix.envlink.activity.common.screen.MainScreen
import com.codeintellix.envlink.activity.common.screen.SplashScreen
import com.codeintellix.envlink.entity.cosnt.ActivityExtra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val showSplash: Boolean =
            intent.getBooleanExtra(ActivityExtra.SHOW_SPLASH_SCREEN_EXTRA, true)

        enableEdgeToEdge()

        if (showSplash) {
            // 显示启动画面并初始化应用
            showSplashScreen()

            // 延迟后初始化应用
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // 强制显示
                initApp()
            }
        } else {
            initApp()
        }
    }

    private fun showSplashScreen() {
        setContent {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SplashScreen()
            }
        }
    }

    private fun initApp() {
        setContent {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MainScreen()
            }
        }
    }
}