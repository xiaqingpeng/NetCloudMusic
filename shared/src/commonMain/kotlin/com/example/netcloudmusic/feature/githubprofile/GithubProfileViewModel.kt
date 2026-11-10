package com.example.netcloudmusic.feature.githubprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.GithubRepository
import com.example.netcloudmusic.core.model.GithubRepo
import com.example.netcloudmusic.core.model.GithubUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface GithubProfileUiState {
    data object Loading : GithubProfileUiState
    data class Success(val user: GithubUser, val repos: List<GithubRepo> = emptyList()) : GithubProfileUiState
    data class Error(val message: String) : GithubProfileUiState
}

class GithubProfileViewModel(
    private val repository: GithubRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GithubProfileUiState>(GithubProfileUiState.Loading)
    val uiState: StateFlow<GithubProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = GithubProfileUiState.Loading
            repository.getUser()
                .catch { _uiState.value = GithubProfileUiState.Error(it.message ?: "加载失败") }
                .collectLatest { user ->
                    _uiState.value = GithubProfileUiState.Success(user)
                    repository.getRepos()
                        .catch { /* 仓库加载失败不影响用户主页 */ }
                        .collectLatest { repos ->
                            val current = _uiState.value
                            if (current is GithubProfileUiState.Success) {
                                _uiState.value = current.copy(repos = repos)
                            }
                        }
                }
        }
    }
}
