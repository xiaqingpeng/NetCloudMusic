package com.example.netcloudmusic.feature.confirmorder

import androidx.compose.runtime.Immutable
import com.example.netcloudmusic.core.model.ConfirmOrderResponse

@Immutable
sealed interface ConfirmOrderUiState {
    data class Success(val confirmOrder: ConfirmOrderResponse) : ConfirmOrderUiState
    data class Error(val res: Int? = null, val throwable: Throwable? = null) : ConfirmOrderUiState
    data object Loading : ConfirmOrderUiState
}
