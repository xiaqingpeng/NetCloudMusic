package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.model.payChannels
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.radio_button
import netcloudmusic.shared.generated.resources.radio_button_checked
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 支付渠道选择器
 *
 * @param payChannel 当前选中的支付渠道
 * @param payChannelChanged 支付渠道变更回调
 */
@Composable
fun PayChannelSelector(
    payChannel: Int,
    payChannelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        payChannels.forEach { channel ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        payChannelChanged(channel.channel)
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Image(
                    painter = painterResource(channel.icon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(channel.title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(
                        if (payChannel == channel.channel)
                            Res.drawable.radio_button_checked
                        else
                            Res.drawable.radio_button
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
