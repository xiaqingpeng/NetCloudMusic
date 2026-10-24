package com.example.netcloudmusic.feature.productdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.CartRepository
import com.example.netcloudmusic.core.data.repository.ProductRepository
import com.example.netcloudmusic.core.exception.tipMessage
import com.example.netcloudmusic.core.model.Cart
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.Spec
import com.example.netcloudmusic.core.network.Network
import com.example.netcloudmusic.core.util.ResourceUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.time.TimeSource

/**
 * 商品详情VM
 *
 * 与 ClientApp 对齐：
 * - 通过 mutableStateOf<ProductDetailOrderRequest?> 触发导航，而非直接调用回调
 * - onAddCartClick 接入购物车 API
 */
class ProductDetailViewModel(
    private val productId: String,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /** 与 ClientApp 一致：用 mutableStateOf 触发确认订单页跳转 */
    val toConfirmOrderPage = mutableStateOf<ProductDetailOrderRequest?>(null)

    val isShowSpecDialog = MutableStateFlow<Boolean>(false)

    val specUiState = MutableStateFlow<ProductSpecUiState>(ProductSpecUiState())

    /** 成功提示资源 id */
    val tipSuccessMessage = MutableStateFlow<String?>(null)

    /** 错误提示文字 */
    val tipError = MutableStateFlow<String?>(null)

    /** 分享文字触发 */
    val toShareText = mutableStateOf<String>("")

    /** 分享图片数据触发 */
    val toShareImage = mutableStateOf<ByteArray?>(null)

    private lateinit var data: Product

    init {
        println("ProductDetailViewModel: init - productId=$productId")
        loadData()
    }

    fun loadData() {
        println("ProductDetailViewModel: loadData 开始")
        viewModelScope.launch {
            val startTime = TimeSource.Monotonic.markNow()

            try {
                println("ProductDetailViewModel: 请求商品详情...")
                val response = productRepository.productDetailSync(productId)
                
                println("ProductDetailViewModel: 收到响应 - isSucceeded=${response.isSucceeded}, status=${response.status}")
                
                val elapsedTime = startTime.elapsedNow().inWholeMilliseconds
                if (elapsedTime < 1000) {
                    delay(1000 - elapsedTime)
                }

                if (!response.isSucceeded) {
                    println("ProductDetailViewModel: 业务失败 - ${response.message}")
                    _uiState.value = ProductDetailUiState.Error(
                        IllegalStateException(response.message ?: "商品详情数据为空")
                    )
                    return@launch
                }

                val product = response.data
                if (product == null) {
                    println("ProductDetailViewModel: 数据为空")
                    _uiState.value = ProductDetailUiState.Error(
                        IllegalStateException(response.message ?: "商品详情数据为空")
                    )
                    return@launch
                }

                println("ProductDetailViewModel: 加载成功 - product.id=${product.id}, title=${product.title}")
                data = product
                _uiState.value = ProductDetailUiState.Success(product)

                // 选中每个规格第一个
                val specs = selectedSpecFirst(product.specs)
                createProductSpecUiState(specs)
            } catch (e: Exception) {
                println("ProductDetailViewModel: 异常 - ${e.message}")
                e.printStackTrace()
                _uiState.value = ProductDetailUiState.Error(e)
            }
        }
    }

    fun reloadData() {
        _uiState.value = ProductDetailUiState.Loading
        loadData()
    }

    /**
     * 与 ClientApp 完全一致：dismissSpecDialog，然后设置 toConfirmOrderPage 触发导航
     */
    fun onConfirmOrderClick() {
        dismissSpecDialog()
        toConfirmOrderPage.value = ProductDetailOrderRequest(
            productId = productId,
            stockId = specUiState.value.selectedStockId,
            count = specUiState.value.count,
        )
    }

    fun clearToConfirmOrder() {
        toConfirmOrderPage.value = null
    }

    fun dismissSpecDialog() {
        isShowSpecDialog.value = false
    }

    fun showSpecDialog() {
        isShowSpecDialog.value = true
    }

    /**
     * 与 ClientApp 完全一致：接入购物车 API
     */
    fun onAddCartClick() {
        viewModelScope.launch {
            val param = Cart(
                productId = productId,
                stockId = specUiState.value.selectedStockId,
                count = specUiState.value.count,
            )

            try {
                val response = cartRepository.addProductToCartSync(param)
                if (response.isSucceeded) {
                    dismissSpecDialog()
                    tipSuccessMessage.value = "加入购物车成功"
                } else {
                    tipError.value = response.message ?: "加入购物车失败"
                }
            } catch (e: Exception) {
                tipError.value = e.tipMessage()
            }
        }
    }

    /**
     * 选择每个规格第一个（安全访问 getOrNull(0)）
     */
    private fun selectedSpecFirst(data: List<Spec>?): List<Spec>? {
        return data?.map {
            it.copy(
                selectedSubId = it.data?.getOrNull(0)?.id,
                selectedSubTitle = it.data?.getOrNull(0)?.title,
            )
        }
    }

    private fun createProductSpecUiState(specs: List<Spec>?) {
        // 过滤 null 再拼接，避免 "null null" 导致 stock 匹配失败
        val specString = specs
            ?.mapNotNull { it.selectedSubTitle }
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(" ")

        val stock = if (specString != null) {
            data.stocks?.firstOrNull { it.title == specString }
        } else {
            data.stocks?.firstOrNull()
        }

        specUiState.value = ProductSpecUiState(
            id = data.id,
            icon = stock?.icon ?: data.icon.orEmpty(),
            price = stock?.price ?: data.price,
            originPrice = stock?.originPrice ?: data.originPrice,
            selectedStockId = stock?.id,
            selectedStockTitle = stock?.title,
            stocks = data.stocks,
            specs = specs,
        )
    }

    fun onSubSpecSelected(specId: String, subSpecId: String, subSpecTitle: String) {
        val specs = specUiState.value.specs!!.map {
            if (it.id == specId) {
                it.copy(
                    selectedSubId = subSpecId,
                    selectedSubTitle = subSpecTitle,
                )
            } else {
                it
            }
        }
        createProductSpecUiState(specs)
    }

    fun onDecrementCount() {
        if (specUiState.value.count <= 1) return
        specUiState.value = specUiState.value.copy(count = specUiState.value.count - 1)
    }

    fun onIncrementCount() {
        if (specUiState.value.count >= 99) return
        specUiState.value = specUiState.value.copy(count = specUiState.value.count + 1)
    }

    fun onShareTextClick() {
        toShareText.value = data.title
    }

    fun onShareImageClick() {
        val icon = data.icon?.takeIf(String::isNotBlank) ?: return
        viewModelScope.launch {
            runCatching {
                Network.imageHttpClient.get(ResourceUtil.r(icon)).body<ByteArray>()
            }.onSuccess {
                toShareImage.value = it
            }.onFailure {
                tipError.value = it.tipMessage()
            }
        }
    }

    fun clearToShareText() {
        toShareText.value = ""
    }

    fun clearToShareImage() {
        toShareImage.value = null
    }

    fun clearTipSuccessMessage() {
        tipSuccessMessage.value = null
    }

    fun clearTipError() {
        tipError.value = null
    }
}
