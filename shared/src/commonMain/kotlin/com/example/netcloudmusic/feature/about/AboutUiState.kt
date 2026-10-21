package com.example.netcloudmusic.feature.about

sealed interface AboutUiState {
    data object Loading : AboutUiState
    data class Success(
        val versionName: String,
        val versionCode: String,
        val cacheSize: String = "计算中...",
        val isClearingCache: Boolean = false,
    ) : AboutUiState
}
