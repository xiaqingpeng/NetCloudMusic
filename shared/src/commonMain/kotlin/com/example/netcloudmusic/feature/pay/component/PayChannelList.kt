package com.example.netcloudmusic.feature.pay.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceOuter
import com.example.netcloudmusic.core.model.PayChannel
import com.example.netcloudmusic.core.model.payChannels
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.radio_button
import netcloudmusic.shared.generated.resources.radio_button_checked
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 支付渠道列表
 */
@Composable
fun PayChannelList(
    selectedChannel: Int,
    onChannelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        payChannels.forEach { channel ->
            PayChannelItem(
                channel = channel,
                isSelected = selectedChannel == channel.channel,
                onChannelClick = { onChannelChanged(channel.channel) },
            )
        }
    }
}

@Composable
private fun PayChannelItem(
    channel: PayChannel,
    isSelected: Boolean,
    onChannelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onChannelClick() }
            .padding(horizontal = SpaceOuter, vertical = SpaceMedium),
    ) {
        Image(
            painter = painterResource(channel.icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = stringResource(channel.title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Image(
            painter = painterResource(
                if (isSelected) Res.drawable.radio_button_checked else Res.drawable.radio_button,
            ),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
        )
    }
}
