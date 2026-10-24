package com.example.netcloudmusic.feature.product.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import com.example.netcloudmusic.core.design.theme.extraSmallTopRoundedCornerShape
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.core.util.ResourceUtil
import com.example.netcloudmusic.core.util.StringUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.placeholder
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.product_item_info
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 网格布局的商品卡片（首页"为你推荐"区域使用）
 */
@Composable
fun ItemProductGrid(
    data: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier) {
            if (data.icon != null) {
                AsyncImage(
                    model = ResourceUtil.r(data.icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(extraSmallTopRoundedCornerShape),
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(extraSmallTopRoundedCornerShape),
                )
            }
            Column(modifier = Modifier.padding(SpaceMedium)) {
                Text(
                    text = data.title,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                SpaceSmallHeight()
                Text(
                    text = data.highlight ?: " ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                SpaceSmallHeight()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.price, data.priceFloat),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    SpaceSmallWidth()
                    Text(
                        text = stringResource(Res.string.price, data.originPriceFloat),
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                SpaceSmallHeight()
                Text(
                    text = stringResource(
                        Res.string.product_item_info,
                        StringUtil.formatCount(data.commentsCount),
                        data.commentsRate.toInt(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}
