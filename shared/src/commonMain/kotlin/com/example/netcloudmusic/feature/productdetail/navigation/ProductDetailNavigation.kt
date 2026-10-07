package com.example.netcloudmusic.feature.productdetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.netcloudmusic.feature.productdetail.ProductDetailOrderRequest
import com.example.netcloudmusic.feature.productdetail.ProductDetailRoute

const val PRODUCT_DETAIL_ROUTE = "product_detail"

fun NavController.navigateToProductDetail(productId: String) {
    navigate(ProductDetailRouteArgs(productId))
}

fun NavGraphBuilder.productDetailScreen(
    toBack: () -> Unit,
    toConfirmOrder: (ProductDetailOrderRequest) -> Unit = {},
    onAddCartClick: (ProductDetailOrderRequest) -> Unit = {},
    onCartClick: () -> Unit = {},
    toChat: (String) -> Unit = {},
    onShareImageClick: () -> Unit = {},
    onShareTextClick: () -> Unit = {},
) {
    composable<ProductDetailRouteArgs> { entry ->
        ProductDetailRoute(
            productId = entry.toRoute<ProductDetailRouteArgs>().productId,
            toBack = toBack,
            toConfirmOrder = toConfirmOrder,
            onAddCartClick = onAddCartClick,
            onCartClick = onCartClick,
            toChat = toChat,
            onShareImageClick = onShareImageClick,
            onShareTextClick = onShareTextClick,
        )
    }
}

@kotlinx.serialization.Serializable
private data class ProductDetailRouteArgs(val productId: String)
