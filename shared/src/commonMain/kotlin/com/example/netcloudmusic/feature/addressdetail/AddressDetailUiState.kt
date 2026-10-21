package com.example.netcloudmusic.feature.addressdetail

sealed interface AddressDetailUiState {
    data object None : AddressDetailUiState
    data object Loading : AddressDetailUiState
    data object Success : AddressDetailUiState
    data class Error(val message: String? = null) : AddressDetailUiState
}
