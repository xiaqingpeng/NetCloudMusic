package com.example.netcloudmusic.feature.discovery.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.bodyXMediumBold
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.feature.component.ItemProduct
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.hot_product
import org.jetbrains.compose.resources.stringResource

/**
 * 热门商品区
 */
@Composable
fun HotProductColumn(
    data: List<Product>,
    toProductDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) return
    
    Column(modifier = modifier.fillMaxWidth()) {
        ProductItemTitle(title = stringResource(Res.string.hot_product))
        data.forEach { product ->
            ItemProduct(
                data = product,
                modifier = Modifier.clickable { toProductDetail(product.id) },
            )
        }
    }
}

@Composable
fun ProductItemTitle(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyXMediumBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = SpaceOuter),
        )
    }
}
