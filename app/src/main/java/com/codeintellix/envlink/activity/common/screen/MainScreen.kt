package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XBackground
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codeintellix.envlink.activity.common.navigation.NavigationTab
import com.codeintellix.envlink.activity.common.navigation.NavigationTabs
import com.codeintellix.envlink.activity.theme.Gray
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@Composable
fun MainScreen() {
    val items = NavigationTabs.getNavigationTabs()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size }
    )
    val coroutineScope = rememberCoroutineScope()
    var currentPage by remember { mutableIntStateOf(0) }

    /*// 启动页面状态
    var isContentVisible by remember { mutableStateOf(false) }

    // 页面加载后直接显示内容（带淡入动画）
    LaunchedEffect(true) {
        isContentVisible = true
    }*/

    // 监听页面变化
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(items, currentPage) { page ->
                currentPage = page
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        }
    ) { innerPadding ->
        XBackground.Space(
            activeColor = items[currentPage].iconColor,
            secondaryColor = null,
            tertiaryColor = null,
            toastMargin = XPadding.horizontal(20).bottom(110)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> EnvLinkPage()
                        1 -> IntelligencePage()
                        2 -> MinePage()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<NavigationTab>,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            drawLine(
                color = Color(255, 255, 255, 50),
                strokeWidth = 1.dp.toPx(),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f)
            )
        }

        NavigationBar(
            containerColor = Color(20, 20, 20, 255)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(vertical = 4.dp),
                            painter = painterResource(id = item.icon),
                            tint = if (currentPage == index) {
                                item.iconColor
                            } else {
                                Gray
                            },
                            contentDescription = item.screen.route
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.text),
                            color = if (currentPage == index) Color.White else Gray
                        )
                    },
                    selected = currentPage == index,
                    colors = NavigationBarItemDefaults.colors(
                        // indicatorColor = Color(24, 24, 52) // 选中时指示器背景色
                        indicatorColor = item.iconColor.withAlpha(0.2f),
                    ),
                    onClick = {
                        onPageSelected(index)
                    }
                )
            }
        }
    }
}