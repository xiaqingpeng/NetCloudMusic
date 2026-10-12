package com.example.netcloudmusic.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun reload() {
        loadProducts()
    }

    fun loadProducts(
        app: Int? = null,
        query: String? = null,
        size: String? = null,
        page: String? = null,
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loadState = 1, errorMessage = null)
            }

            try {
                val resp = productRepository.products(
                    app = app,
                    query = query,
                    size = size,
                    page = page,
                )

                if (!resp.isSucceeded) {
                    throw IllegalStateException(resp.message ?: "加载失败")
                }

              // delay(3000L)

                _uiState.update {
                    it.copy(
                        datum = resp.data?.list.orEmpty(),
                        loadState = 3,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadState = 2,
                        errorMessage = e.message ?: "加载失败"
                    )
                }
            }
        }
    }
}
