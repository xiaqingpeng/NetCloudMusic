package com.example.netcloudmusic.feature.login

sealed interface LoginUiState {
    data object None : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}
