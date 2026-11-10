package com.example.netcloudmusic.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.ThemeMode
import com.example.netcloudmusic.core.data.repository.ThemeRepository
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.data.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val userRepository: UserRepository,
    private val githubRepository: GithubRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        themeRepository.themeMode
            .onEach { mode -> _uiState.value = _uiState.value.copy(themeMode = mode) }
            .launchIn(viewModelScope)

        // 与「我的」页一致：跟随 UserRepository.userState，避免只清本地存储导致 Me 仍显示已登录
        userRepository.userState
            .onEach { user ->
                _uiState.value = _uiState.value.copy(isLoggedIn = user != null)
            }
            .launchIn(viewModelScope)
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { themeRepository.setThemeMode(mode) }
    }

    fun logout() {
        githubRepository.logout()
        userRepository.logout()
        _uiState.value = _uiState.value.copy(isLoggedIn = false, loggedOut = true)
    }

    fun consumeLoggedOut() {
        _uiState.value = _uiState.value.copy(loggedOut = false)
    }
}
