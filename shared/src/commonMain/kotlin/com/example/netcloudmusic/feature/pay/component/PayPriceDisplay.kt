package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.price
import org.jetbrains.compose.resources.stringResource

/**
 * 支付金额显示
 *
 * @param priceFloat 订单金额（元）
 */
@Composable
fun PayPriceDisplay(
    priceFloat: Double,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.price, priceFloat),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
