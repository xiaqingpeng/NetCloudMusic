package com.example.netcloudmusic.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.CommonRepository
import com.example.netcloudmusic.core.data.repository.ProductRepository
import com.example.netcloudmusic.core.exception.tipMessage
import com.example.netcloudmusic.core.model.response.NetworkPageMeta
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

const val TAG = "DiscoveryViewModel"

/**
 * 发现界面 ViewModel
 *
 * - 并发请求 indexes（Banner/按钮/大图/热门） + products（商品列表）
 * - 支持下拉刷新（isRefreshing）
 * - 支持上拉加载更多（LoadMoreState）
 * - 首次加载与下拉刷新状态独立，不再共用同一个整数码
 */
class DiscoveryViewModel(
    private val commonRepository: CommonRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    private var pageMeta: NetworkPageMeta? = null

    companion object {
        /** 最短加载时间（毫秒），避免加载太快闪烁 */
        const val EXPECTED_CONSUME_TIME = 1000L
    }

    init {
        println("DiscoveryViewModel: init 块执行，开始加载数据")
        loadData()
    }

    /**
     * 加载首页数据。
     * 首次调用时显示全屏 loading（isInitialLoading），
     * 后续下拉刷新时只显示 PullToRefresh 指示器（isRefreshing）。
     */
    fun loadData() {
        println("DiscoveryViewModel: loadData 开始执行")
        
        // 防止重复触发
        if (_uiState.value.isRefreshing) {
            println("DiscoveryViewModel: 正在刷新中，跳过本次请求")
            return
        }

        val isFirstLoad = _uiState.value.topDatum.isEmpty()
        println("DiscoveryViewModel: isFirstLoad=$isFirstLoad")

        viewModelScope.launch {
            val startTime = TimeSource.Monotonic.markNow()
            pageMeta = null

            _uiState.value = _uiState.value.copy(
                isInitialLoading = isFirstLoad,
                isRefreshing = !isFirstLoad,
                refreshResult = RefreshResult.Idle,
                loadMoreState = LoadMoreState.Idle,
            )
            
            println("DiscoveryViewModel: 开始并发请求 indexes 和 products")

            // 并发请求首页区块数据和第一页商品列表
            val indexJob    = async { 
                println("DiscoveryViewModel: 开始请求 indexes")
                runCatching { commonRepository.indexes() }
            }
            val productsJob = async { 
                println("DiscoveryViewModel: 开始请求 products")
                runCatching { productRepository.productsSync(page = 1, size = 10) }
            }

            val indexResult    = indexJob.await()
            val productsResult = productsJob.await()
            
            println("DiscoveryViewModel: indexes 结果: ${indexResult.isSuccess}")
            println("DiscoveryViewModel: products 结果: ${productsResult.isSuccess}")

            // 确保最短加载时间，避免 loading 闪烁
            val elapsed = startTime.elapsedNow().inWholeMilliseconds
            if (elapsed < EXPECTED_CONSUME_TIME) delay(EXPECTED_CONSUME_TIME - elapsed)

            // 网络异常
            if (indexResult.isFailure || productsResult.isFailure) {
                val throwable = indexResult.exceptionOrNull() ?: productsResult.exceptionOrNull()
                println("DiscoveryViewModel: 网络异常 - ${throwable?.message}")
                throwable?.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isInitialLoading = false,
                    isRefreshing = false,
                    refreshResult = RefreshResult.Error,
                    errorMessage = throwable?.tipMessage() ?: "加载失败",
                )
                return@launch
            }

            val indexData    = indexResult.getOrNull()!!
            val productsData = productsResult.getOrNull()!!
            
            println("DiscoveryViewModel: indexData.isSucceeded=${indexData.isSucceeded}, productsData.isSucceeded=${productsData.isSucceeded}")
            println("DiscoveryViewModel: indexData.status=${indexData.status}, indexData.message=${indexData.message}")
            println("DiscoveryViewModel: indexData.data?.list?.size=${indexData.data?.list?.size}")
            println("DiscoveryViewModel: productsData.data?.list?.size=${productsData.data?.list?.size}")

            // 业务错误处理：至少需要一个接口成功
            val hasIndexData = indexData.isSucceeded && indexData.data?.list?.isNotEmpty() == true
            val hasProductsData = productsData.isSucceeded && productsData.data?.list?.isNotEmpty() == true
            
            if (!hasIndexData && !hasProductsData) {
                // 两个接口都失败或都没数据
                val message = if (!indexData.isSucceeded) {
                    indexData.message ?: "加载失败"
                } else if (!productsData.isSucceeded) {
                    productsData.message ?: "加载失败"
                } else {
                    "暂无数据"
                }
                println("DiscoveryViewModel: 两个接口都失败 - $message")
                _uiState.value = _uiState.value.copy(
                    isInitialLoading = false,
                    isRefreshing = false,
                    refreshResult = RefreshResult.Error,
                    errorMessage = message,
                )
                return@launch
            }

            // 成功：至少有一个接口返回了数据
            println("DiscoveryViewModel: 数据加载成功！hasIndexData=$hasIndexData, hasProductsData=$hasProductsData")
            _uiState.value = _uiState.value.copy(
                isInitialLoading = false,
                isRefreshing = false,
                refreshResult = if (isFirstLoad) RefreshResult.Idle else RefreshResult.Success,
                topDatum = if (hasIndexData) indexData.data?.list.orEmpty() else emptyList(),
                datum    = if (hasProductsData) productsData.data?.list.orEmpty() else emptyList(),
            )
            pageMeta = productsData.data?.pagination
            
            println("DiscoveryViewModel: 最终状态 - topDatum.size=${_uiState.value.topDatum.size}, datum.size=${_uiState.value.datum.size}")

            // 刷新成功 toast 短暂展示后重置
            if (!isFirstLoad) {
                delay(2000)
                _uiState.value = _uiState.value.copy(refreshResult = RefreshResult.Idle)
            }
        }
    }

    /** 清除错误状态（toast 关闭后调用） */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            refreshResult = RefreshResult.Idle,
            errorMessage = null,
        )
    }

    /**
     * 上拉加载更多
     */
    fun loadMore() {
        if (_uiState.value.loadMoreState == LoadMoreState.NoMore) return
        if (_uiState.value.loadMoreState == LoadMoreState.Loading) return

        _uiState.value = _uiState.value.copy(loadMoreState = LoadMoreState.Loading)

        viewModelScope.launch {
            val startTime = TimeSource.Monotonic.markNow()
            val nextPage  = pageMeta?.next ?: 2

            try {
                val response = productRepository.productsSync(page = nextPage, size = 10)
                
                val elapsed = startTime.elapsedNow().inWholeMilliseconds
                if (elapsed < EXPECTED_CONSUME_TIME) delay(EXPECTED_CONSUME_TIME - elapsed)

                if (response.isSucceeded) {
                    pageMeta = response.data?.pagination

                    // 短暂显示"加载成功"后切换到最终态
                    _uiState.value = _uiState.value.copy(
                        loadMoreState = LoadMoreState.Done,
                        datum = _uiState.value.datum + response.data?.list.orEmpty(),
                    )
                    delay(1000)
                    _uiState.value = _uiState.value.copy(
                        loadMoreState = if (pageMeta?.next == null) LoadMoreState.NoMore else LoadMoreState.Idle,
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        loadMoreState = LoadMoreState.Error,
                        errorMessage  = response.message ?: "加载失败",
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadMoreState = LoadMoreState.Error,
                    errorMessage  = e.tipMessage(),
                )
            }
        }
    }
}
