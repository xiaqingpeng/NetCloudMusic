package com.example.netcloudmusic.feature.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.platform.clearCache
import com.example.netcloudmusic.core.platform.getCacheSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 关于页面 ViewModel（KMP 共享层版本）
 *
 * - 版本号通过构造参数注入（由 AppContainerProvider 提供）
 * - 缓存大小通过 expect/actual CacheManager 获取
 * - WorkManager 下载 APK 属于 Android 专有，在 KMP 层暂不实现
 */
class AboutViewModel(
    private val context: Any? = null,
    versionName: String = "",
    versionCode: String = "",
) : ViewModel() {

    private val _uiState = MutableStateFlow<AboutUiState>(
        AboutUiState.Success(versionName = versionName, versionCode = versionCode)
    )
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        loadCacheSize()
    }

    private fun loadCacheSize() {
        viewModelScope.launch {
            val size = withContext(Dispatchers.Default) { getCacheSize(context) }
            _uiState.update { state ->
                if (state is AboutUiState.Success) state.copy(cacheSize = size) else state
            }
        }
    }

    fun onClearCacheClick() {
        val state = _uiState.value as? AboutUiState.Success ?: return
        if (state.isClearingCache) return
        viewModelScope.launch {
            _uiState.update { (it as? AboutUiState.Success)?.copy(isClearingCache = true) ?: it }
            withContext(Dispatchers.Default) { clearCache(context) }
            val newSize = withContext(Dispatchers.Default) { getCacheSize(context) }
            _uiState.update { s ->
                (s as? AboutUiState.Success)?.copy(cacheSize = newSize, isClearingCache = false) ?: s
            }
        }
    }
}
