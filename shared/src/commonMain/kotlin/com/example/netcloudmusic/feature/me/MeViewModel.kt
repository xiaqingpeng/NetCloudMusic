package com.example.netcloudmusic.feature.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        userRepository.userState
            .onEach { user -> _uiState.value = MeUiState(user != null, user) }
            .launchIn(viewModelScope)
    }

    fun refresh() = userRepository.refreshUserState()
    fun logout() = userRepository.logout()
}

data class MeUiState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
)
