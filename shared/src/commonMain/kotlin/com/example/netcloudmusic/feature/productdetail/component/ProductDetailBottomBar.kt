package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.design.theme.SpaceSmall
import com.example.netcloudmusic.core.design.theme.largeEndRoundedCornerShape
import com.example.netcloudmusic.core.design.theme.largeStartRoundedCornerShape
import com.example.netcloudmusic.core.design.theme.md_theme_light_primary30
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.add_cart
import netcloudmusic.shared.generated.resources.buy_now
import org.jetbrains.compose.resources.stringResource

/**
 * 商品详情底部操作栏
 *
 * [left] 插槽用于放置"客服""购物车"等图标按钮，为 null 时只显示主操作按钮。
 */
@Composable
fun MyProductDetailBottomBar(
    modifier: Modifier = Modifier,
    toConfirmOrder: () -> Unit = {},
    onAddCartClick: () -> Unit = {},
    left: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter, vertical = SpaceSmall),
    ) {
        left?.let { it() }

        // 加入购物车
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(largeStartRoundedCornerShape)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(MaterialTheme.colorScheme.primary, md_theme_light_primary30)
                    )
                )
                .clickable { onAddCartClick() },
        ) {
            Text(
                text = stringResource(Res.string.add_cart),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        // 立即购买
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(largeEndRoundedCornerShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { toConfirmOrder() },
        ) {
            Text(
                text = stringResource(Res.string.buy_now),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
