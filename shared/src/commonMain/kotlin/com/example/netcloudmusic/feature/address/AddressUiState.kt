package com.example.netcloudmusic.feature.address

import com.example.netcloudmusic.core.model.Address

sealed interface AddressUiState {
    data class Success(val datum: List<Address>) : AddressUiState
    data class Error(val message: String? = null) : AddressUiState
    data object Loading : AddressUiState
    data object None : AddressUiState
}
