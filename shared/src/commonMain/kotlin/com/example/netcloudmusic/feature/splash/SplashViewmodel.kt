package com.example.netcloudmusic.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.AppPreferenceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 启动界面 ViewModel
 *
 * 倒计时结束后按是否已看过引导分流：引导页 / 主页。
 */
class SplashViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
) : ViewModel() {

    private var timerJob: Job? = null

    private val _timeLeft = MutableStateFlow(3L)
    val timeLeft: StateFlow<Long> = _timeLeft.asStateFlow()

    private val _navigateToGuide = MutableStateFlow(false)
    val navigateToGuide: StateFlow<Boolean> = _navigateToGuide.asStateFlow()

    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain: StateFlow<Boolean> = _navigateToMain.asStateFlow()

    init {
        startCountDown()
    }

    private fun startCountDown() {
        timerJob = viewModelScope.launch {
            repeat(3) { second ->
                _timeLeft.value = 3L - second
                delay(1000)
            }
            onSkipClick()
        }
    }

    /**
     * 跳过或倒计时结束：按是否已看过引导分流
     */
    fun onSkipClick() {
        timerJob?.cancel()
        viewModelScope.launch {
            if (appPreferenceRepository.isNotShowGuide()) {
                _navigateToMain.value = true
            } else {
                _navigateToGuide.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
