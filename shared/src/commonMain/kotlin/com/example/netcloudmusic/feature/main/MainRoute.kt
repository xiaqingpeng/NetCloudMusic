package com.example.netcloudmusic.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.design.component.MyNavigationBar
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.feature.cart.CartRoute
import com.example.netcloudmusic.feature.category.CategoryRoute
import com.example.netcloudmusic.feature.discovery.DiscoveryRoute
import com.example.netcloudmusic.feature.main.component.MainDrawerContent
import com.example.netcloudmusic.feature.main.navigation.TopLevelDestination
import com.example.netcloudmusic.feature.me.MeRoute
import com.example.netcloudmusic.feature.shortvideo.ShortVideoRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * 主界面
 */
@Composable
fun MainRoute(
    toProductDetail: (String) -> Unit = {},
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
    toConfirmOrder: (List<String>) -> Unit = {},
    viewModel: MainViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MainViewModel(AppContainerProvider.instance.userRepository)
            }
        },
    ),
) {
    MainScreen(
        viewModel = viewModel,
        toProductDetail = toProductDetail,
        toCategoryDetail = toCategoryDetail,
        toLogin = toLogin,
        toGithubProfile = toGithubProfile,
        toSettings = toSettings,
        toProfile = toProfile,
        toReceivingAddress = toReceivingAddress,
        toAbout = toAbout,
        toOrder = toOrder,
        toScan = toScan,
        toCode = toCode,
        toConfirmOrder = toConfirmOrder,
    )
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    toProductDetail: (String) -> Unit = {},
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
    toConfirmOrder: (List<String>) -> Unit = {},
) {
    var currentDestination by remember { mutableStateOf(TopLevelDestination.DISCOVERY.route) }
    val pagerState = rememberPagerState { TopLevelDestination.entries.size }
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val isGithubLogin = user?.style == 20

    fun closeDrawer() {
        coroutineScope.launch { drawerState.close() }
    }

    fun toggleDrawer() {
        coroutineScope.launch {
            if (drawerState.isClosed) drawerState.open() else drawerState.close()
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { currentDestination = TopLevelDestination.entries[it].route }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
            ) {
                MainDrawerContent(
                    toLogin = toLogin,
                    toSettings = toSettings,
                    toReceivingAddress = toReceivingAddress,
                    toAbout = toAbout,
                    toProfile = toProfile,
                    toGithubProfile = toGithubProfile,
                    toOrder = { style ->
                        // 抽屉入口同样要求商城业务 session，避免 GitHub 登录后进订单必然失败
                        if (AppContainerProvider.instance.userRepository.hasBusinessSession()) {
                            toOrder(style)
                        } else {
                            closeDrawer()
                            toLogin()
                        }
                    },
                    user = user,
                    isLogin = isLoggedIn,
                    isGithubLogin = isGithubLogin,
                    closeDrawer = ::closeDrawer,
                )
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MainContent(
                modifier = Modifier.weight(1f),
                pagerState = pagerState,
                toggleDrawer = ::toggleDrawer,
                toProductDetail = toProductDetail,
                toCategoryDetail = toCategoryDetail,
                toLogin = toLogin,
                toGithubProfile = toGithubProfile,
                toSettings = toSettings,
                toProfile = toProfile,
                toReceivingAddress = toReceivingAddress,
                toAbout = toAbout,
                toOrder = toOrder,
                toScan = toScan,
                toCode = toCode,
                toConfirmOrder = toConfirmOrder,
            )
            MyNavigationBar(
                modifier = Modifier,
                destinations = TopLevelDestination.entries.toList(),
                onNavigateToDestination = {
                    currentDestination = TopLevelDestination.entries[it].route
                    coroutineScope.launch { pagerState.animateScrollToPage(it) }
                },
                currentDestination = currentDestination,
            )
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier,
    pagerState: PagerState,
    toggleDrawer: () -> Unit = {},
    toProductDetail: (String) -> Unit = {},
    toCategoryDetail: (com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg) -> Unit = {},
    toLogin: () -> Unit = {},
    toGithubProfile: () -> Unit = {},
    toSettings: () -> Unit = {},
    toProfile: () -> Unit = {},
    toReceivingAddress: () -> Unit = {},
    toAbout: () -> Unit = {},
    toOrder: (Int) -> Unit = {},
    toScan: () -> Unit = {},
    toCode: () -> Unit = {},
    toConfirmOrder: (List<String>) -> Unit = {},
) {
    HorizontalPager(state = pagerState, modifier = modifier.fillMaxWidth()) {
        when (it) {
            0 -> DiscoveryRoute(
                toProductDetail = toProductDetail,
                toggleDrawer = toggleDrawer,
            )
            1 -> ShortVideoRoute()
            2 -> CategoryRoute(toCategoryDetail)
            3 -> CartRoute(
                toProductDetail = toProductDetail,
                toConfirmOrder = toConfirmOrder,
            )
            4 -> MeRoute(
                toLogin = toLogin,
                toGithubProfile = toGithubProfile,
                toSettings = toSettings,
                toProfile = toProfile,
                toReceivingAddress = toReceivingAddress,
                toAbout = toAbout,
                toOrder = toOrder,
                toScan = toScan,
                toCode = toCode,
            )
            else -> Unit
        }
    }
}
