package com.example.netcloudmusic.feature.productdetail

import com.example.netcloudmusic.core.model.Product

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class Success(val product: Product) : ProductDetailUiState
    data class Error(val data: Throwable) : ProductDetailUiState
}
