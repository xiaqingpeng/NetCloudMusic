package com.example.netcloudmusic.feature.productdetail

import com.example.netcloudmusic.core.model.Spec
import com.example.netcloudmusic.core.model.Stock

data class ProductSpecUiState(
    val productId: String = "",
    val icon: String? = null,
    val price: Int = 0,
    val originPrice: Int = 0,
    val selectedStockId: String? = null,
    val selectedStockTitle: String? = null,
    val stocks: List<Stock> = emptyList(),
    val specs: List<Spec> = emptyList(),
    val count: Int = 1,
) {
    val priceText: String get() = formatCents(price)
    val originPriceText: String get() = formatCents(originPrice)
}

private fun formatCents(value: Int): String {
    val absolute = value.toLong().let { if (it < 0) -it else it }
    return (if (value < 0) "-" else "") + (absolute / 100) + "." +
        (absolute % 100).toString().padStart(2, '0')
}
