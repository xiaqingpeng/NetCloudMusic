package com.example.netcloudmusic.feature.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.util.StringUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val COUNTDOWN_SECONDS = 60

class ForgetPasswordViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    // 重置密码主流程状态
    private val _uiState = MutableStateFlow<ForgetPasswordUiState>(ForgetPasswordUiState.None)
    val uiState: StateFlow<ForgetPasswordUiState> = _uiState.asStateFlow()

    // 发送验证码按钮状态
    private val _sendCodeState = MutableStateFlow<ForgetSendCodeUiState>(ForgetSendCodeUiState.Idle)
    val sendCodeState: StateFlow<ForgetSendCodeUiState> = _sendCodeState.asStateFlow()

    // 输入框数据
    private val _data = MutableStateFlow(ForgetPasswordUiModel())
    val data: StateFlow<ForgetPasswordUiModel> = _data.asStateFlow()

    /**
     * 发送验证码成功后弹 Toast 的一次性事件
     * 格式："手机号|验证码" 或 "手机号"；null 表示无待消费事件
     */
    private val _sendCodeToast = MutableStateFlow<String?>(null)
    val sendCodeToast: StateFlow<String?> = _sendCodeToast.asStateFlow()

    private var countdownJob: Job? = null

    // ─────────────────────────────────────────────────────────────────────────
    // 输入框更新
    // ─────────────────────────────────────────────────────────────────────────

    fun onValueChange(newData: ForgetPasswordUiModel) {
        _data.value = newData
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 发送验证码
    // ─────────────────────────────────────────────────────────────────────────

    fun onSendCodeClick() {
        val phone = _data.value.phone.trim()

        if (phone.isBlank()) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("请输入手机号") }
            return
        }
        if (!StringUtil.isPhone(phone)) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("手机号格式不正确") }
            return
        }

        viewModelScope.launch {
            _sendCodeState.update { ForgetSendCodeUiState.Sending }
            try {
                userRepository.sendForgotPasswordCode(phone).collect { response ->
                    if (response.isSucceeded) {
                        println("ForgetPasswordVM: 验证码发送成功：$phone, code=${response.data?.code}")
                        _sendCodeToast.value = buildString {
                            append(phone)
                            response.data?.code?.let { append("|$it") }
                        }
                        startCountdown()
                    } else {
                        println("ForgetPasswordVM: 验证码发送失败：${response.message}")
                        _sendCodeState.update { ForgetSendCodeUiState.Error(response.message) }
                    }
                }
            } catch (e: Exception) {
                println("ForgetPasswordVM: 验证码发送异常: ${e.message}")
                _sendCodeState.update { ForgetSendCodeUiState.Error(e.message) }
            }
        }
    }

    fun consumeSendCodeToast() {
        _sendCodeToast.value = null
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (remain in COUNTDOWN_SECONDS downTo 1) {
                _sendCodeState.update { ForgetSendCodeUiState.Countdown(remain) }
                delay(1_000L)
            }
            _sendCodeState.update { ForgetSendCodeUiState.Idle }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 确认重置密码
    // ─────────────────────────────────────────────────────────────────────────

    fun onConfirmClick() {
        val phone = _data.value.phone.trim()
        val code = _data.value.code.trim()
        val newPassword = _data.value.newPassword
        val confirmPassword = _data.value.confirmPassword

        // 手机号
        if (phone.isBlank()) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("请输入手机号") }
            return
        }
        if (!StringUtil.isPhone(phone)) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("手机号格式不正确") }
            return
        }

        // 验证码
        if (code.isBlank()) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("请输入验证码") }
            return
        }
        if (!StringUtil.isCode(code)) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("验证码格式不正确") }
            return
        }

        // 新密码
        if (newPassword.isBlank()) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("请输入密码") }
            return
        }
        if (!StringUtil.isPassword(newPassword)) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("密码格式不正确（6-20位字母数字组合）") }
            return
        }

        // 确认密码
        if (confirmPassword.isBlank()) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("请再次输入密码") }
            return
        }
        if (newPassword != confirmPassword) {
            _uiState.update { ForgetPasswordUiState.ErrorMessage("两次输入的密码不一致") }
            return
        }

        viewModelScope.launch {
            _uiState.update { ForgetPasswordUiState.Loading }
            try {
                println("ForgetPasswordVM: 开始重置密码 - phone=$phone, verifyCode=$code")
                userRepository.resetPassword(phone, code, newPassword).collect { resp ->
                    println("ForgetPasswordVM: API响应 - isSucceeded=${resp.isSucceeded}, message=${resp.message}")
                    if (resp.isSucceeded) {
                        println("ForgetPasswordVM: 密码重置成功")
                        _uiState.update { ForgetPasswordUiState.Success }
                    } else {
                        val errorMsg = resp.message ?: "密码重置失败，请检查验证码是否正确"
                        println("ForgetPasswordVM: 密码重置失败：$errorMsg")
                        _uiState.update {
                            ForgetPasswordUiState.Error(
                                throwable = Exception(errorMsg),
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                println("ForgetPasswordVM: 密码重置异常: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    ForgetPasswordUiState.Error(throwable = e)
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 状态重置
    // ─────────────────────────────────────────────────────────────────────────

    fun clearError() {
        _uiState.update { state ->
            when (state) {
                is ForgetPasswordUiState.ErrorMessage,
                is ForgetPasswordUiState.Error -> ForgetPasswordUiState.None
                else -> state
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}
