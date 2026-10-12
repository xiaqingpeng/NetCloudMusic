package com.example.netcloudmusic.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.None)
    val uiState = _uiState.asStateFlow()

    fun onLoginClick(username: String, password: String) {
        when {
            username.isBlank() -> {
                _uiState.value = LoginUiState.Error("请输入手机号或邮箱")
                return
            }
            password.isBlank() -> {
                _uiState.value = LoginUiState.Error("请输入密码")
                return
            }
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                userRepository.login(username.trim(), password).collect { response ->
                    _uiState.value = if (response.isSucceeded) {
                        LoginUiState.Success
                    } else {
                        LoginUiState.Error(response.message ?: "登录失败，请检查用户名和密码")
                    }
                }
            } catch (error: Throwable) {
                _uiState.value = LoginUiState.Error(error.message ?: "网络请求失败")
            }
        }
    }

    fun clearError() {
        _uiState.value = LoginUiState.None
    }
}
