package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceExtraOuter
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.feature.productdetail.ProductSpecUiState
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.product_item_info
import org.jetbrains.compose.resources.stringResource

/** 商品价格、标题、亮点、评论数等基本信息区 */
@Composable
fun ProductDetailInfo(data: Product, specUiState: ProductSpecUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(SpaceOuter),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.price, specUiState.priceFloat),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            SpaceSmallWidth()
            Text(
                text = "¥${specUiState.originPriceFloat}",
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = MaterialTheme.colorScheme.outline,
            )
        }

        Text(
            text = data.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        data.highlight?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = SpaceMedium),
            )
        }

        Text(
            text = stringResource(Res.string.product_item_info, data.commentsCount, data.commentsRate),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = SpaceMedium),
        )
    }
}

/** 带标题、值、右箭头的设置行（发货地、店铺、规格等） */
@Composable
fun MyProductSettingItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter, vertical = SpaceExtraOuter),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.widthIn(60.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
