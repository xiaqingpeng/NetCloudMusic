package com.example.netcloudmusic.feature.main.navigation

import androidx.compose.runtime.Immutable
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.tab_mall_cart
import netcloudmusic.shared.generated.resources.tab_mall_cart_selected
import netcloudmusic.shared.generated.resources.tab_mall_category
import netcloudmusic.shared.generated.resources.tab_mall_category_selected
import netcloudmusic.shared.generated.resources.tab_mall_main
import netcloudmusic.shared.generated.resources.tab_mall_main_selected
import netcloudmusic.shared.generated.resources.tab_mall_me
import netcloudmusic.shared.generated.resources.tab_mall_me_selected
import netcloudmusic.shared.generated.resources.tab_mall_video
import netcloudmusic.shared.generated.resources.tab_mall_video_selected
import netcloudmusic.shared.generated.resources.cart
import netcloudmusic.shared.generated.resources.category
import netcloudmusic.shared.generated.resources.home
import netcloudmusic.shared.generated.resources.me
import netcloudmusic.shared.generated.resources.video
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import com.example.netcloudmusic.feature.cart.navigation.CART_ROUTE
import com.example.netcloudmusic.feature.category.navigation.CATEGORY_ROUTE
import com.example.netcloudmusic.feature.discovery.navigation.DISCOVERY_ROUTE
import com.example.netcloudmusic.feature.me.navigation.ME_ROUTE
import com.example.netcloudmusic.feature.shortvideo.navigation.SHORT_VIDEO_ROUTE

/**
 * 底层界面模型
 * 
 * 在 KMP 中，图标和文本资源需要在平台层实现
 * 这里只定义路由信息
 */
@Immutable
enum class TopLevelDestination(
    val route: String,
    val title: StringResource,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
) {
    //发现界面
    DISCOVERY(
        route = DISCOVERY_ROUTE,
        title = Res.string.home,
        selectedIcon = Res.drawable.tab_mall_main_selected,
        unselectedIcon = Res.drawable.tab_mall_main
    ),
    SHORT_VIDEO(
        route = SHORT_VIDEO_ROUTE,
        title = Res.string.video,
        selectedIcon = Res.drawable.tab_mall_video_selected,
        unselectedIcon = Res.drawable.tab_mall_video
    ),
    CATEGORY(
        route = CATEGORY_ROUTE,
        title = Res.string.category,
        selectedIcon = Res.drawable.tab_mall_category_selected,
        unselectedIcon = Res.drawable.tab_mall_category
    ),
    CART(
        route = CART_ROUTE,
        title = Res.string.cart,
        selectedIcon = Res.drawable.tab_mall_cart_selected,
        unselectedIcon = Res.drawable.tab_mall_cart
    ),
    ME(
        route = ME_ROUTE,
        title = Res.string.me,
        selectedIcon = Res.drawable.tab_mall_me_selected,
        unselectedIcon = Res.drawable.tab_mall_me
    ),
}
