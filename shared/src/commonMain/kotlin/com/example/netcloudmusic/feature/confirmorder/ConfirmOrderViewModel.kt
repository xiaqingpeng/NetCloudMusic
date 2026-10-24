package com.example.netcloudmusic.feature.confirmorder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.OrderRepository
import com.example.netcloudmusic.core.model.OrderRequest
import com.example.netcloudmusic.feature.confirmorder.navigation.ConfirmOrderParam
import com.example.netcloudmusic.core.util.Constant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfirmOrderViewModel(
    private val orderRepository: OrderRepository,
    initialParam: ConfirmOrderParam,
) : ViewModel() {

    // 与 ClientApp 保持一致：使用 mutableStateOf 而非 StateFlow
    // Route 层通过 LaunchedEffect(viewModel.toPay.value) 监听
    val toPay = mutableStateOf<String?>(null)

    // 与 ClientApp BaseViewModel 保持一致
    val tipErrorRes = MutableStateFlow<Int?>(null)

    private var addressId: String? = null

    private val param = MutableStateFlow(initialParam)

    // 与 ClientApp 完全一致：param 变化时通过网络请求处理响应
    val uiState = param.flatMapLatest { p ->
        println("ConfirmOrderVM: confirmOrder param: productId=${p.productId} stockId=${p.stockId} count=${p.count}")
        val req = OrderRequest(
            productId = p.productId,
            stockId = p.stockId,
            addressId = p.addressId,
            carts = p.carts,
            count = p.count,
            source = Constant.IOS,
        )
        orderRepository.confirmOrder(req)
            .map { response ->
                if (response.isSucceeded) {
                    val data = response.data
                    if (data == null) {
                        println("ConfirmOrderVM: confirmOrder data is null: status=${response.status} message=${response.message}")
                        return@map ConfirmOrderUiState.Error()
                    }
                    addressId = data.address?.id
                    ConfirmOrderUiState.Success(data)
                } else {
                    println("ConfirmOrderVM: confirmOrder failed: ${response.message}")
                    ConfirmOrderUiState.Error(throwable = Exception(response.message))
                }
            }
            .catch { e ->
                println("ConfirmOrderVM: confirmOrder flow error: ${e.message}")
                emit(ConfirmOrderUiState.Error(throwable = e))
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfirmOrderUiState.Loading,
    )

    fun onPrimaryClick() {
        val addressId = param.value.addressId ?: addressId
        if (addressId.isNullOrEmpty()) {
            // TODO: 使用资源ID
            tipErrorRes.value = 0
            return
        }

        viewModelScope.launch {
            orderRepository.createOrder(
                OrderRequest(
                    productId = param.value.productId,
                    addressId = addressId,
                    stockId = param.value.stockId,
                    count = param.value.count,
                    carts = param.value.carts,
                    source = Constant.IOS,
                )
            ).collectLatest {
                if (it.isSucceeded) {
                    toPay.value = it.data!!.id
                } else {
                    // TODO: 提示下单失败错误
                }
            }
        }
    }

    fun onAddressChange(addressId: String?) {
        param.value = param.value.copy(
            addressId = addressId,
        )
    }

    fun clearToPay() {
        toPay.value = null
    }

    fun clearTipErrorRes() {
        tipErrorRes.value = null
    }
}
