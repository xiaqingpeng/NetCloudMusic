package com.example.netcloudmusic.feature.cart.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceMediumWidth
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.core.design.theme.SpacerOuterWidth
import com.example.netcloudmusic.core.model.Cart
import com.example.netcloudmusic.core.util.ResourceUtil
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.radio_button
import netcloudmusic.shared.generated.resources.radio_button_checked
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartItem(
    data: Cart,
    modifier: Modifier = Modifier,
    toProductDetail: (String) -> Unit = {},
    itemCheckChanged: (Cart) -> Unit = {},
    onDecrementCount: (Cart) -> Unit = {},
    onIncrementCount: (Cart) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter, vertical = SpaceMedium),
    ) {
        // 选中状态图标
        Image(
            painter = painterResource(
                if (data.selected) Res.drawable.radio_button_checked else Res.drawable.radio_button
            ),
            contentDescription = null,
            modifier = Modifier.clickable { itemCheckChanged(data) },
        )

        SpacerOuterWidth()

        // 商品图片
        AsyncImage(
            model = ResourceUtil.r(data.product!!.icon!!),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(110.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable { toProductDetail(data.product.id) }
                .background(LocalDividerColor.current),
        )

        SpaceMediumWidth()

        Column(modifier = Modifier.weight(1f)) {
            // 商品标题
            Text(
                text = data.product.title,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            SpaceSmallHeight()

            // 规格（有选规格时才显示）
            data.stock?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(LocalDividerColor.current, CircleShape)
                        .padding(horizontal = SpaceMedium),
                ) {
                    Text(
                        text = it.title!!,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            SpaceSmallHeight()

            // 价格 + 数量控件
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.price, data.product.priceFloat),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                // 简单的数量控件
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onDecrementCount(data) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "减少"
                        )
                    }
                    Text(
                        text = data.count.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = { onIncrementCount(data) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "增加"
                        )
                    }
                }
            }
        }
    }
}
