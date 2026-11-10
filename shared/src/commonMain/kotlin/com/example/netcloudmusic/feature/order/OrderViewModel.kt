package com.example.netcloudmusic.feature.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.OrderRepository
import com.example.netcloudmusic.core.model.Order
import com.example.netcloudmusic.core.model.response.NetworkPageMeta
import com.example.netcloudmusic.core.network.NetworkSession
import com.example.netcloudmusic.core.util.Constant
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository,
    initialSelectedIndex: Int = 0,
) : ViewModel() {

    private val _datum = MutableStateFlow<List<Order>>(emptyList())
    val datum: StateFlow<List<Order>> = _datum

    // Me 页「退款/售后」会传 style=4，而订单 Tab 只有 0..3，需要夹紧避免越界崩溃
    private val _selectedIndex = MutableStateFlow(initialSelectedIndex.coerceIn(0, indicatorValues.lastIndex))
    val selectedIndex: StateFlow<Int> = _selectedIndex

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.None)
    val loadingState: StateFlow<LoadingState> = _loadingState

    private var pagination: NetworkPageMeta? = null
    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun loadData() {
        loadJob?.cancel()
        _datum.value = emptyList()
        pagination = null
        _loadingState.value = LoadingState.None
        loadMore()
    }

    fun loadMore() {
        if (_loadingState.value == LoadingState.Loading) return
        if (_loadingState.value == LoadingState.NoMoreData) return

        // 无商城业务 session 时不发无用请求（GitHub 本地登录不会写入 NetworkSession）
        val session = NetworkSession.session
        if (session.isBlank() || session.startsWith("github_")) {
            _loadingState.value = LoadingState.Error("请先登录商城账号")
            return
        }

        val selectedStatus = indicatorValues[_selectedIndex.value]
        loadJob = viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            delay(500)
            try {
                val params = mutableMapOf<String, String>()
                if (selectedStatus != Constant.VALUE_NO) {
                    params[Constant.STATUS] = selectedStatus.toString()
                }
                params[Constant.PAGE] = if (pagination != null) {
                    ((pagination!!.page ?: 0) + 1).toString()
                } else "1"

                val response = orderRepository.orders(params)
                if (response.isSucceeded) {
                    val list = response.data?.list ?: emptyList()
                    _datum.value = _datum.value + list
                    pagination = response.data?.pagination
                    val page = pagination?.page ?: 0
                    val pages = pagination?.pages ?: 0
                    _loadingState.value = if (page >= pages) LoadingState.NoMoreData else LoadingState.None
                } else {
                    _loadingState.value = LoadingState.Error(response.message ?: "加载失败")
                }
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Error(e.message ?: "加载失败")
            }
        }
    }

    fun selectedIndexChanged(index: Int) {
        _selectedIndex.value = index.coerceIn(0, indicatorValues.lastIndex)
        loadData()
    }

    companion object {
        private val indicatorValues = intArrayOf(
            Constant.VALUE_NO,
            Constant.WAIT_PAY,
            Constant.WAIT_RECEIVED,
            Constant.WAIT_COMMENT,
        )
    }
}
