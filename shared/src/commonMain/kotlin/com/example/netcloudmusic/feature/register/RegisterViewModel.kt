package com.example.netcloudmusic.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 注册界面 ViewModel
 */
class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.None)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _data = MutableStateFlow(RegisterUiModel())
    val data: StateFlow<RegisterUiModel> = _data.asStateFlow()

    fun onValueChange(newData: RegisterUiModel) {
        _data.value = newData
    }

    fun onRegisterClick() {
        val model = _data.value

        // 基础校验
        if (model.nickname.isBlank()) { setError("请输入昵称"); return }
        if (model.phone.isBlank()) { setError("请输入手机号"); return }
        if (!model.phone.matches(Regex("^1\\d{10}$"))) { setError("手机号格式不正确"); return }
        if (model.email.isBlank()) { setError("请输入邮箱"); return }
        if (!model.email.contains('@')) { setError("邮箱格式不正确"); return }
        if (model.password.isBlank()) { setError("请输入密码"); return }
        if (model.password.length < 6) { setError("密码至少6位"); return }
        if (model.confirmPassword.isBlank()) { setError("请输入确认密码"); return }
        if (model.password != model.confirmPassword) { setError("两次输入的密码不一致"); return }

        val param = User(
            nickname = model.nickname,
            phone = model.phone,
            email = model.email,
            password = model.password,
        )
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                userRepository.register(param).collect { response ->
                    _uiState.value = if (response.isSucceeded) {
                        RegisterUiState.Success
                    } else {
                        RegisterUiState.Error(response.message ?: "注册失败")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "网络请求失败")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = RegisterUiState.None
    }

    private fun setError(message: String) {
        _uiState.value = RegisterUiState.Error(message)
    }
}
