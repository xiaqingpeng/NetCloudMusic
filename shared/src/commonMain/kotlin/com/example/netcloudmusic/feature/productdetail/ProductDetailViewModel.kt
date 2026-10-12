package com.example.netcloudmusic.feature.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.ProductRepository
import com.example.netcloudmusic.core.exception.CommonException
import com.example.netcloudmusic.core.exception.tipMessage
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.model.Spec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: String,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _specUiState = MutableStateFlow(ProductSpecUiState())
    val specUiState = _specUiState.asStateFlow()
    private val _isShowSpecDialog = MutableStateFlow(false)
    val isShowSpecDialog = _isShowSpecDialog.asStateFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            val startedAt = kotlin.time.TimeSource.Monotonic.markNow()
            _uiState.value = ProductDetailUiState.Loading
            try {
                val response = productRepository.productDetail(productId)
                val remaining = 1000L - startedAt.elapsedNow().inWholeMilliseconds
                if (remaining > 0) delay(remaining)
                if (!response.isSucceeded) throw CommonException(networkResponse = response)
                val product = response.data
                    ?: throw CommonException(networkResponse = response)
                _uiState.value = ProductDetailUiState.Success(product)
                updateSpecState(product, selectFirstOptions(product.specs.orEmpty()), resetCount = true)
            } catch (error: Throwable) {
                _uiState.value = ProductDetailUiState.Error(error)
            }
        }
    }

    fun reloadData() = loadData()
    fun showSpecDialog() { _isShowSpecDialog.value = true }
    fun dismissSpecDialog() { _isShowSpecDialog.value = false }

    fun onSubSpecSelected(specId: String, optionId: String) {
        val product = (_uiState.value as? ProductDetailUiState.Success)?.product ?: return
        val selected = _specUiState.value.specs.map { spec ->
            if (spec.id != specId) spec else {
                val option = spec.data.orEmpty().firstOrNull { it.id == optionId }
                spec.copy(selectedSubId = option?.id, selectedSubTitle = option?.title)
            }
        }
        updateSpecState(product, selected)
    }

    fun decrementCount() {
        _specUiState.value = _specUiState.value.copy(count = (_specUiState.value.count - 1).coerceAtLeast(1))
    }

    fun incrementCount() {
        _specUiState.value = _specUiState.value.copy(count = (_specUiState.value.count + 1).coerceAtMost(99))
    }

    private fun selectFirstOptions(specs: List<Spec>): List<Spec> = specs.map { spec ->
        val first = spec.data.orEmpty().firstOrNull()
        spec.copy(selectedSubId = first?.id, selectedSubTitle = first?.title)
    }

    private fun updateSpecState(product: Product, specs: List<Spec>, resetCount: Boolean = false) {
        val title = specs.mapNotNull { it.selectedSubTitle }
            .joinToString(" ")
            .takeIf(String::isNotBlank)
        val normalizedTitle = title?.normalizeStockTitle()
        val stock = normalizedTitle?.let { selectedTitle ->
            product.stocks.orEmpty().firstOrNull {
                it.title.normalizeStockTitle() == selectedTitle
            }
        }
        _specUiState.value = ProductSpecUiState(
            productId = product.id,
            icon = stock?.icon ?: product.icon,
            price = stock?.price ?: product.price,
            originPrice = stock?.originPrice ?: product.originPrice,
            selectedStockId = stock?.id,
            selectedStockTitle = stock?.title,
            stocks = product.stocks.orEmpty(),
            specs = specs,
            count = if (resetCount) 1 else _specUiState.value.count,
        )
    }

    /** 后端库存标题偶尔会包含重复空格或使用不同的空白字符。 */
    private fun String?.normalizeStockTitle(): String = this.orEmpty()
        .replace(Regex("\\s+"), " ")
        .trim()
}
