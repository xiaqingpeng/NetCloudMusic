package com.example.netcloudmusic.feature.productdetail

import androidx.compose.runtime.Immutable
import com.example.netcloudmusic.core.model.Spec
import com.example.netcloudmusic.core.model.Stock

/**
 * 商品规格选择状态
 */
@Immutable
data class ProductSpecUiState(
    val id: String = "",
    val icon: String = "",
    val price: Int = 0,
    val originPrice: Int = 0,
    val selectedStockId: String? = null,
    val selectedStockTitle: String? = null,
    val stocks: List<Stock>? = null,
    val specs: List<Spec>? = null,
    val count: Int = 1
) {
    val priceFloat: Float
        get() = price / 100.0f

    val originPriceFloat: Float
        get() = originPrice / 100.0f
}
