package com.example.netcloudmusic.feature.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CodeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            val localUser = userRepository.getLocalUser()
            if (localUser?.id.isNullOrBlank()) return@launch
            userRepository.userDetail(localUser!!.id!!).collect { response ->
                _user.value = response.data ?: localUser
            }
        }
    }
}
