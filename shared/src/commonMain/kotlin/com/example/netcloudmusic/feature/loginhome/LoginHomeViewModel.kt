package com.example.netcloudmusic.feature.loginhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface ThirdPartyLoginUiState {
    data object Idle : ThirdPartyLoginUiState
    data object Loading : ThirdPartyLoginUiState
    data object Success : ThirdPartyLoginUiState
    data class Error(val message: String) : ThirdPartyLoginUiState
}

/**
 * LoginHome ViewModel。
 *
 * 不依赖 Android SDK；平台层通过回调把 GitHub 授权结果注入这里。
 */
class LoginHomeViewModel(
    private val githubRepository: GithubRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ThirdPartyLoginUiState>(ThirdPartyLoginUiState.Idle)
    val uiState: StateFlow<ThirdPartyLoginUiState> = _uiState.asStateFlow()

    val tipError = MutableStateFlow<String?>(null)

    /** GitHub OAuth 回调拿到 code 后调用。 */
    fun onGithubCode(code: String) {
        viewModelScope.launch {
            _uiState.value = ThirdPartyLoginUiState.Loading
            githubRepository.loginWithCode(code)
                .catch { e ->
                    val message = e.message ?: "GitHub 登录失败"
                    _uiState.value = ThirdPartyLoginUiState.Error(message)
                    tipError.value = message
                }
                .collectLatest {
                    _uiState.value = ThirdPartyLoginUiState.Success
                }
        }
    }

    fun onThirdPartyError(message: String) {
        tipError.value = message
        _uiState.value = ThirdPartyLoginUiState.Idle
    }

    fun clearError() {
        tipError.value = null
        if (_uiState.value is ThirdPartyLoginUiState.Error) {
            _uiState.value = ThirdPartyLoginUiState.Idle
        }
    }
}
