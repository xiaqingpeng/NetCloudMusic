package com.example.netcloudmusic.feature.categorydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.example.netcloudmusic.feature.categorydetail.CategoryDetailArg
import com.example.netcloudmusic.feature.categorydetail.CategoryDetailRoute
import com.example.netcloudmusic.ui.navigation.myComposable
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val CATEGORY_DETAIL_ARG = "category_detail_arg"
const val CATEGORY_DETAIL_ROUTE = "category_detail"

/**
 * 跳转到三级子分类详情页
 * @param arg 包含三级子分类完整数据及一、二级分类 id 的参数对象
 */
@OptIn(ExperimentalEncodingApi::class)
fun NavController.navigateToCategoryDetail(arg: CategoryDetailArg) {
    val json = Json.encodeToString(arg)
    // Path 参数只能使用 URL-safe 字符，标准 Base64 中的 `+`、`/` 会导致
    // Navigation 无法匹配 category_detail/{category_detail_arg}。
    val encoded = Base64.UrlSafe.encode(json.toByteArray())
    navigate("${CATEGORY_DETAIL_ROUTE}/$encoded") {
        launchSingleTop = true
    }
}

/**
 * 注册三级子分类详情页路由
 */
@OptIn(ExperimentalEncodingApi::class)
fun NavGraphBuilder.categoryDetailScreen(
    toBack: () -> Unit = {},
    toProductDetail: (String) -> Unit = {},
) {
    myComposable(
        route = "${CATEGORY_DETAIL_ROUTE}/{${CATEGORY_DETAIL_ARG}}",
        arguments = listOf(
            navArgument(CATEGORY_DETAIL_ARG) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val encoded = backStackEntry.arguments
            ?.read { getStringOrNull(CATEGORY_DETAIL_ARG) }
            .orEmpty()
        val json = Base64.UrlSafe.decode(encoded).decodeToString()
        val arg = Json.decodeFromString<CategoryDetailArg>(json)

        CategoryDetailRoute(
            arg = arg,
            toBack = toBack,
            toProductDetail = toProductDetail,
        )
    }
}
