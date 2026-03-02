package aethex.matrix.foundation.theme

import aethex.matrix.foundation.color.XBackgroundColor
import aethex.matrix.foundation.color.XContentColor
import aethex.matrix.foundation.color.XTextFieldColor
import aethex.matrix.foundation.color.XThemeColor
import aethex.matrix.foundation.typography.XTypography
import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * ▼ 主题
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2024.7.2
 * Updated by Wu Qizhen on 2025.7.23
 */
@Composable
fun XTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    setupStatusBar: Boolean = false, // 默认不处理状态栏
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = XThemeColor.DARK,
            onPrimary = XThemeColor.BASE,
            secondary = Color.Black,
            onSecondary = Color.White,
            tertiary = Color.Black,
            onTertiary = Color.White,
            background = Color.Black,
            onBackground = Color.White,
            surface = Color.Black,
            onSurface = Color.White,
            inverseSurface = Color.White,
            inverseOnSurface = Color.Gray,
            error = XContentColor.RED_DARK,
            onError = XBackgroundColor.RED_DARK
        )

        else -> lightColorScheme(
            primary = XThemeColor.DARK,
            onPrimary = XThemeColor.LIGHT,
            secondary = Color.White,
            onSecondary = Color.Black,
            tertiary = Color.White,
            onTertiary = Color.Black,
            background = Color.White,
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
            inverseSurface = Color.Gray,
            inverseOnSurface = Color.White,
            error = XContentColor.RED_DARK,
            onError = XBackgroundColor.RED_DARK
        )
    }

    val view = LocalView.current
    val context = LocalContext.current

    if (setupStatusBar && !view.isInEditMode) {
        SideEffect {
            /*val window = (view.context as Activity).window
            // 关键修改：强制设置为黑色
            // window.statusBarColor = Color.Black.toArgb()
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            // 确保状态栏图标可见（亮色图标）
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme*/

            val window = (context as? Activity)?.window ?: return@SideEffect

            // 透明状态栏，让内容延伸到状态栏下
            // window.statusBarColor = Color.Transparent.toArgb()
            // window.navigationBarColor = Color.Transparent.toArgb()

            // 设置边衬区处理模式
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // 根据主题设置状态栏图标颜色
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme

            // 处理刘海屏
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = XTypography.TYPOGRAPHY
    ) {
        // 在 MaterialTheme 内部覆盖 CompositionLocal
        CompositionLocalProvider(
            LocalTextSelectionColors provides XTextFieldColor.TEXT_FIELD_SELECTION
        ) {
            // 应用您的内容
            content()
        }
    }
}