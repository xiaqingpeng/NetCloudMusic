package com.example.netcloudmusic.feature.productdetail

import androidx.compose.runtime.Stable
import com.example.netcloudmusic.core.model.Product

/**
 * 商品详情界面状态
 */
@Stable
sealed interface ProductDetailUiState {
    /**
     * 成功
     */
    data class Success(val data: Product) : ProductDetailUiState

    /**
     * 异常错误
     */
    data class Error(
        val data: Throwable
    ) : ProductDetailUiState

    /**
     * 加载中
     */
    data object Loading : ProductDetailUiState
}
