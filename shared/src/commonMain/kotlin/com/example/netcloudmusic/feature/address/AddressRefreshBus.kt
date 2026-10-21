package com.example.netcloudmusic.feature.address

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 地址保存成功后通知地址列表刷新的单向总线。
 * 由 AddressDetailNavigation.onSaveSuccess 写入 true，
 * AddressViewModel 订阅后自动刷新并重置。
 */
object AddressRefreshBus {
    private val _needRefresh = MutableStateFlow(false)
    val needRefresh: StateFlow<Boolean> = _needRefresh.asStateFlow()

    fun trigger() { _needRefresh.value = true }
    fun consume() { _needRefresh.value = false }
}
