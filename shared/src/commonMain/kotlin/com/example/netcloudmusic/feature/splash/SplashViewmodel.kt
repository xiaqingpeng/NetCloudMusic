package com.example.netcloudmusic.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 启动界面vm
 */
class SplashViewmodel : ViewModel() {

    private var timerJob: Job? = null

    private val _timeLeft = MutableStateFlow(3L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain: StateFlow<Boolean> = _navigateToMain

    init {
        startCountDown()
    }

    /**
     * 启动倒计时
     */
    private fun startCountDown() {
        timerJob = viewModelScope.launch {
            repeat(3) { second ->
                _timeLeft.value = 3L - second
                delay(1000)
            }
            navigateToMain()
        }
    }

    /**
     * 跳过倒计时，跳转到主界面
     */
    fun onSkipClick() {
        navigateToMain()
    }

    /**
     * 导航到主界面
     */
    private fun navigateToMain() {
        timerJob?.cancel()
        _navigateToMain.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
