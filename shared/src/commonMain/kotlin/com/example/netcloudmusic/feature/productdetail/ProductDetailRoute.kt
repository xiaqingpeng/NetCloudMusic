package com.example.netcloudmusic.feature.productdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoading
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.exception.tipIcon
import com.example.netcloudmusic.core.exception.tipMessage
import com.example.netcloudmusic.feature.productdetail.component.MyContentView
import com.example.netcloudmusic.feature.productdetail.component.MyProductSpecDialog
import com.example.netcloudmusic.ui.MyTipPlaceholder
import com.example.netcloudmusic.core.util.rememberShareLauncher

/**
 * 商品详情界面入口
 *
 * 负责：ViewModel 绑定、副作用（导航跳转、分享）、Tip 提示。
 * UI 骨架委托给 [ProductDetailScreen]。
 */
@Composable
fun ProductDetailRoute(
    productId: String,
    toBack: () -> Unit = {},
    toConfirmOrder: (ProductDetailOrderRequest) -> Unit = {},
) {
    val viewModel: ProductDetailViewModel = viewModel(
        key = "product-detail-$productId",
        factory = viewModelFactory {
            initializer {
                ProductDetailViewModel(
                    productId = productId,
                    productRepository = AppContainerProvider.instance.productRepository,
                    cartRepository = AppContainerProvider.instance.cartRepository,
                )
            }
        }
    )

    val uiState        by viewModel.uiState.collectAsState()
    val specUiState    by viewModel.specUiState.collectAsState()
    val isShowSpecDialog by viewModel.isShowSpecDialog.collectAsState()
    val tipSuccessMessage  by viewModel.tipSuccessMessage.collectAsState()
    val tipError       by viewModel.tipError.collectAsState()
    val shareLauncher = rememberShareLauncher()

    ProductDetailScreen(
        toBack           = toBack,
        uiState          = uiState,
        specUiState      = specUiState,
        isShowSpecDialog = isShowSpecDialog,
        showSpecDialog   = viewModel::showSpecDialog,
        dismissSpecDialog = viewModel::dismissSpecDialog,
        onRetryClick     = viewModel::reloadData,
        toConfirmOrder   = viewModel::onConfirmOrderClick,
        onAddCartClick   = viewModel::onAddCartClick,
        onSubSpecSelected = viewModel::onSubSpecSelected,
        onDecrementCount = viewModel::onDecrementCount,
        onIncrementCount = viewModel::onIncrementCount,
        onShareImageClick = viewModel::onShareImageClick,
        onShareTextClick = viewModel::onShareTextClick,
    )

    MyTipPlaceholder(
        tipSuccessMessage  = tipSuccessMessage,
        clearTipSuccessMessage = viewModel::clearTipSuccessMessage,
        tipError           = tipError,
        clearTipError      = viewModel::clearTipError,
    )

    // 跳转确认订单页
    LaunchedEffect(viewModel.toConfirmOrderPage.value) {
        viewModel.toConfirmOrderPage.value?.let {
            toConfirmOrder(it)
            viewModel.clearToConfirmOrder()
        }
    }

    // 分享文字
    LaunchedEffect(viewModel.toShareText.value) {
        if (viewModel.toShareText.value.isNotBlank()) {
            shareLauncher.shareText(viewModel.toShareText.value)
            viewModel.clearToShareText()
        }
    }

    // 分享图片
    LaunchedEffect(viewModel.toShareImage.value) {
        viewModel.toShareImage.value?.let {
            shareLauncher.shareImage(it)
            viewModel.clearToShareImage()
        }
    }
}

/**
 * 商品详情界面骨架
 *
 * 根据 [uiState] 切换：加载中 / 错误视图 / [MyContentView]。
 */
@Composable
fun ProductDetailScreen(
    uiState: ProductDetailUiState,
    specUiState: ProductSpecUiState = ProductSpecUiState(),
    toBack: () -> Unit = {},
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    onShareImageClick: () -> Unit = {},
    onShareTextClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    isShowSpecDialog: Boolean = false,
    showSpecDialog: () -> Unit = {},
    dismissSpecDialog: () -> Unit = {},
    onSubSpecSelected: (String, String, String) -> Unit = { _, _, _ -> },
    onDecrementCount: () -> Unit = {},
    onIncrementCount: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ProductDetailUiState.Success -> MyContentView(
                data = state.data,
                specUiState = specUiState,
                toBack = toBack,
                toConfirmOrder = showSpecDialog,
                onAddCartClick = showSpecDialog,
                showSpecDialog = showSpecDialog,
                onShareImageClick = onShareImageClick,
                onShareTextClick = onShareTextClick,
            )
            is ProductDetailUiState.Loading -> MyLoading()
            is ProductDetailUiState.Error   -> MyErrorView(
                message      = state.data.tipMessage(),
                icon         = state.data.tipIcon(),
                onRetryClick = onRetryClick,
            )
        }
    }

    if (isShowSpecDialog) {
        MyProductSpecDialog(
            data = specUiState,
            onDismissRequest = dismissSpecDialog,
            toConfirmOrder = toConfirmOrder,
            onAddCartClick = onAddCartClick,
            onSubSpecSelected = onSubSpecSelected,
            onDecrementCount = onDecrementCount,
            onIncrementCount = onIncrementCount,
        )
    }
}
