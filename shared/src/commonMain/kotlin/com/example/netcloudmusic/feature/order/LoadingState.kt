package com.example.netcloudmusic.feature.order

sealed interface LoadingState {
    data object None : LoadingState
    data object Loading : LoadingState
    data class Error(val data: String) : LoadingState
    data object NoMoreData : LoadingState
}
