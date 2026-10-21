package com.example.netcloudmusic.feature.codelogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val COUNTDOWN_SECONDS = 60

class CodeLoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CodeLoginUiState>(CodeLoginUiState.None)
    val uiState: StateFlow<CodeLoginUiState> = _uiState.asStateFlow()

    private val _sendCodeState = MutableStateFlow<SendCodeUiState>(SendCodeUiState.Idle)
    val sendCodeState: StateFlow<SendCodeUiState> = _sendCodeState.asStateFlow()

    private val _data = MutableStateFlow(CodeLoginUiModel())
    val data: StateFlow<CodeLoginUiModel> = _data.asStateFlow()

    /** 发送验证码成功的一次性 Toast，用 Channel 确保不丢失、不重复 */
    private val _sendCodeToastChannel = Channel<String>(Channel.BUFFERED)
    val sendCodeToast = _sendCodeToastChannel.receiveAsFlow()

    private var countdownJob: Job? = null

    fun onValueChange(newData: CodeLoginUiModel) { _data.value = newData }

    // ── 发送验证码 ─────────────────────────────────────────────────────────────

    fun onSendCodeClick() {
        // 防重复：发送中或倒计时中不允许再次触发
        val current = _sendCodeState.value
        if (current is SendCodeUiState.Sending || current is SendCodeUiState.Countdown) return

        val phone = _data.value.phone.trim()
        if (phone.isBlank()) { showError("请输入手机号"); return }
        if (!phone.matches(Regex("^1\\d{10}$"))) { showError("手机号格式不正确"); return }

        viewModelScope.launch {
            _sendCodeState.update { SendCodeUiState.Sending }
            try {
                userRepository.sendCode(phone).collect { response ->
                    if (response.isSucceeded) {
                        val toastMsg = buildString {
                            append(phone)
                            response.data?.code?.let { append("|$it") }
                        }
                        _sendCodeToastChannel.send(toastMsg)
                        startCountdown()
                    } else {
                        _sendCodeState.update { SendCodeUiState.Error(response.message) }
                    }
                }
            } catch (e: Exception) {
                _sendCodeState.update { SendCodeUiState.Error(e.message) }
            }
        }
    }

    fun consumeSendCodeToast() { /* no-op，Channel 已自动消费 */ }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (remain in COUNTDOWN_SECONDS downTo 1) {
                _sendCodeState.update { SendCodeUiState.Countdown(remain) }
                delay(1_000L)
            }
            _sendCodeState.update { SendCodeUiState.Idle }
        }
    }

    // ── 验证码登录 ─────────────────────────────────────────────────────────────

    fun onLoginClick() {
        // 防重复：登录中不再触发
        if (_uiState.value is CodeLoginUiState.Loading) return

        val phone = _data.value.phone.trim()
        val code = _data.value.code.trim()

        if (phone.isBlank()) { showError("请输入手机号"); return }
        if (!phone.matches(Regex("^1\\d{10}$"))) { showError("手机号格式不正确"); return }
        if (code.isBlank()) { showError("请输入验证码"); return }
        if (code.length != 6) { showError("验证码为6位数字"); return }

        viewModelScope.launch {
            _uiState.update { CodeLoginUiState.Loading }
            try {
                userRepository.loginByCode(phone, code).collect { response ->
                    _uiState.update {
                        if (response.isSucceeded) CodeLoginUiState.Success
                        else CodeLoginUiState.Error(response.message ?: "登录失败，请检查验证码")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { CodeLoginUiState.Error(e.message ?: "网络请求失败") }
            }
        }
    }

    // ── 状态管理 ───────────────────────────────────────────────────────────────

    private fun showError(message: String) {
        _uiState.update { CodeLoginUiState.Error(message) }
    }

    fun clearError() {
        _uiState.update { if (it is CodeLoginUiState.Error) CodeLoginUiState.None else it }
    }

    fun resetUiState() = clearError()

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}
