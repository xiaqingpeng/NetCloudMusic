package com.example.netcloudmusic.feature.order.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.model.Order
import com.example.netcloudmusic.core.util.SuperDateUtil
import com.example.netcloudmusic.core.util.formatPrice
import com.example.netcloudmusic.feature.confirmorder.ProductContainer
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.go_pay
import netcloudmusic.shared.generated.resources.total_count
import org.jetbrains.compose.resources.stringResource

/**
 * 订单列表单项：时间/状态、商品、合计、去支付
 */
@Composable
fun OrderItem(
    data: Order,
    modifier: Modifier = Modifier,
    toPayOrder: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = SpaceOuter, vertical = SpaceMedium),
        ) {
            Text(
                text = SuperDateUtil.yyyyMMddHHmmss(data.created),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = data.statusText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LocalDividerColor.current),
        )
        ProductContainer(data.carts.orEmpty())
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LocalDividerColor.current),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpaceOuter, vertical = SpaceMedium),
        ) {
            Text(
                text = stringResource(
                    Res.string.total_count,
                    data.carts.orEmpty().sumOf { it.count },
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "¥${formatPrice(data.priceFloat)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SpaceOuter, end = SpaceOuter, bottom = SpaceOuter),
        ) {
            OutlinedButton(
                onClick = { toPayOrder(data.id) },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.outline,
                ),
                modifier = Modifier.height(35.dp),
            ) {
                Text(
                    stringResource(Res.string.go_pay),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
