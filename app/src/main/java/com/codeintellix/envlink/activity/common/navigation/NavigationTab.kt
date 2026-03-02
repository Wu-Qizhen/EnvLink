package com.codeintellix.envlink.activity.common.navigation

import androidx.compose.ui.graphics.Color
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.activity.theme.OrangeRed
import com.codeintellix.envlink.activity.theme.Yellow

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.12
 */
data class NavigationTab(
    val screen: Screen,
    val icon: Int,
    val iconColor: Color,
    val text: Int
)

object NavigationTabs {
    fun getNavigationTabs(): List<NavigationTab> = listOf(
        NavigationTab(Screen.EnvLink, R.drawable.ic_env_link, LightGreen, R.string.env_link),
        NavigationTab(Screen.Intelligence, R.drawable.ic_ai, Yellow, R.string.intelligence),
        NavigationTab(Screen.Mine, R.drawable.ic_smile, OrangeRed, R.string.mine)
    )
}