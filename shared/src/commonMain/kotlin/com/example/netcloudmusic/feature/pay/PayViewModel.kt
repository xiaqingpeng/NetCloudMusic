package com.example.netcloudmusic.feature.pay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.OrderRepository
import com.example.netcloudmusic.core.model.PayRequest
import com.example.netcloudmusic.core.util.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PayViewModel(
    private val orderRepository: OrderRepository,
    private val orderId: String,
    private val style: Int = Constant.VALUE0,
) : ViewModel() {

    var payChannel by mutableIntStateOf(Constant.ALIPAY)
        private set

    var nextValue by mutableStateOf<PayNext?>(null)
        private set

    var toAlipay by mutableStateOf("")
        private set

    val tipErrorRes = MutableStateFlow<Int?>(null)
    val loading = MutableStateFlow(false)

    private val _uiState = MutableStateFlow<PayUiState>(PayUiState.Loading)
    val uiState: StateFlow<PayUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            orderRepository.orderDetail(orderId)
                .collectLatest { result ->
                    if (result.isSucceeded) {
                        val data = result.data!!
                        if (data.isShipped) {
                            // 已经支付了
                            nextValue = PayNext(
                                orderId = orderId,
                                style = style,
                            )
                        }
                        _uiState.value = PayUiState.Success(data)
                    } else {
                        _uiState.value = PayUiState.Error(throwable = Exception(result.message))
                    }
                }
        }
    }

    fun payChannelChanged(data: Int) {
        payChannel = data
    }

    fun onPrimaryClick() {
        viewModelScope.launch {
            loading.value = true

            orderRepository
                .orderPay(
                    PayRequest(
                        id = orderId,
                        channel = payChannel,
                        origin = Constant.ANDROID,
                    )
                )
                .collectLatest { result ->
                    loading.value = false

                    if (result.isSucceeded) {
                        val data = result.data!!
                        processPay(data)
                    } else {
                        // TODO: 使用资源ID
                        tipErrorRes.value = 0
                    }
                }
        }
    }

    private fun processPay(data: com.example.netcloudmusic.core.model.PayResponse) {
        when (data.channel) {
            Constant.ALIPAY -> processAlipay(data.pay!!)
            Constant.WECHAT -> processWechat(data.wechatPay!!)
        }
    }

    private fun processAlipay(payInfo: String) {
        toAlipay = payInfo
    }

    private fun processWechat(data: com.example.netcloudmusic.core.model.WechatPay) {
        // TODO: 微信支付需要平台特定实现，使用 expect/actual 模式
        println("PayViewModel: Wechat pay not implemented in KMP yet")
    }

    fun processAlipayStatusChanged(resultStatus: String) {
        when (resultStatus) {
            "9000" -> { // AlipayResult.RESULT_STATUS_SUCCESS
                viewModelScope.launch {
                    loading.value = true
                    delay(3000)
                    loading.value = false
                    loadData()
                }
            }
            "6001" -> { // AlipayResult.RESULT_STATUS_CANCEL
                // TODO: 使用资源ID
                tipErrorRes.value = 0
            }
            else -> {
                // TODO: 使用资源ID
                tipErrorRes.value = 0
            }
        }
    }
}

data class PayNext(
    val orderId: String,
    val style: Int,
)
