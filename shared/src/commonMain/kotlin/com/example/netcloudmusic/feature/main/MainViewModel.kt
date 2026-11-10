package com.example.netcloudmusic.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * 主界面 ViewModel：观察登录态，供抽屉展示用户信息。
 */
class MainViewModel(
    userRepository: UserRepository,
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.userState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = userRepository.getLocalUser(),
        )

    val isLoggedIn: StateFlow<Boolean> = userRepository.userState
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = userRepository.isLoggedIn(),
        )
}
