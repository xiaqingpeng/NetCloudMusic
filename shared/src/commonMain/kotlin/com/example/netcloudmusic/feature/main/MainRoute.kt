package com.example.netcloudmusic.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.netcloudmusic.core.design.component.MyNavigationBar
import com.example.netcloudmusic.feature.cart.CartRoute
import com.example.netcloudmusic.feature.category.CategoryRoute
import com.example.netcloudmusic.feature.discovery.DiscoveryRoute
import com.example.netcloudmusic.feature.main.navigation.TopLevelDestination
import com.example.netcloudmusic.feature.me.MeRoute
import com.example.netcloudmusic.feature.shortvideo.ShortVideoRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * 主界面
 */
@Composable
fun MainRoute(toProductDetail: (String) -> Unit = {}) {
    MainScreen(toProductDetail)
}

@Composable
fun MainScreen(toProductDetail: (String) -> Unit = {}) {
    /**
     * 当前界面选中的route
     */
    var currentDestination by remember { mutableStateOf(TopLevelDestination.DISCOVERY.route) }
    val pagerState = rememberPagerState {
        TopLevelDestination.entries.size
    }
    val coroutineScope = rememberCoroutineScope()

    /**
     * 手指滑动 Pager 时，同步底部导航选中状态
     */
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect {
                currentDestination = TopLevelDestination.entries[it].route
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MainContent(modifier = Modifier.weight(1f), pagerState = pagerState, toProductDetail = toProductDetail)
        MyNavigationBar(
            modifier = Modifier,
            destinations = TopLevelDestination.entries.toList(),
            onNavigateToDestination = {
                currentDestination = TopLevelDestination.entries[it].route
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            },
            currentDestination = currentDestination
        )
    }
}

@Composable
fun MainContent(
    modifier: Modifier,
    pagerState: PagerState,
    toProductDetail: (String) -> Unit = {},
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
    ) {
        when (it) {
            0 -> DiscoveryRoute(toProductDetail)
            1 -> ShortVideoRoute()
            2 -> CategoryRoute()
            3 -> CartRoute()
            4 -> MeRoute()
            else -> Unit
        }
    }
}
