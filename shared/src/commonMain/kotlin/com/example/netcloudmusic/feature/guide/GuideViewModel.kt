package com.example.netcloudmusic.feature.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.AppPreferenceRepository
import kotlinx.coroutines.launch

/**
 * 引导页 ViewModel
 */
class GuideViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
) : ViewModel() {

    /** 标记已看过引导，下次启动不再展示 */
    fun setNotShowGuide() {
        viewModelScope.launch {
            appPreferenceRepository.setNotShowGuide(true)
        }
    }
}
