package com.example.netcloudmusic.feature.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderId: String,
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = orderRepository.orderDetailSync(orderId)
                if (response.isSucceeded && response.data != null) {
                    _uiState.value = OrderDetailUiState.Success(response.data!!)
                } else {
                    _uiState.value = OrderDetailUiState.Error(response.message ?: "加载失败")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error(e.message ?: "加载失败")
            }
        }
    }
}
