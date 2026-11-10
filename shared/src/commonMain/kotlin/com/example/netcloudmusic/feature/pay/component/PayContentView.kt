package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.model.Order
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.price
import org.jetbrains.compose.resources.stringResource

/**
 * 支付内容视图：金额 + 渠道选择
 */
@Composable
fun PayContentView(
    data: Order,
    payChannel: Int,
    payChannelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SpaceLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(SpaceLarge))

        // 支付金额
        Text(
            text = stringResource(Res.string.price, data.priceFloat.toFloat()),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
            ),
            color = MaterialTheme.colorScheme.primary,
        )

        PayChannelList(
            selectedChannel = payChannel,
            onChannelChanged = payChannelChanged,
        )
    }
}
