package com.example.netcloudmusic.feature.pay

import com.example.netcloudmusic.core.model.Order
import androidx.compose.runtime.Immutable

@Immutable
sealed interface PayUiState {
    data class Success(val order: Order) : PayUiState

    data class Error(
        val res: Int? = null,
        val throwable: Throwable? = null,
    ) : PayUiState

    data object Loading : PayUiState
}
