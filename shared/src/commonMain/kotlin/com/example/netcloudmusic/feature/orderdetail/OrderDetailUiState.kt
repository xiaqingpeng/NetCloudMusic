package com.example.netcloudmusic.feature.orderdetail

import androidx.compose.runtime.Immutable
import com.example.netcloudmusic.core.model.Order

@Immutable
sealed interface OrderDetailUiState {
    data class Success(val data: Order) : OrderDetailUiState
    data class Error(val data: String) : OrderDetailUiState
    data object Loading : OrderDetailUiState
}
