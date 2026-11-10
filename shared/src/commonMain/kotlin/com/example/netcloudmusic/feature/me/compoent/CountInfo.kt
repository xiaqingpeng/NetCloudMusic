package com.example.netcloudmusic.feature.me.compoent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall
import com.example.netcloudmusic.core.design.theme.SpaceLarge
import com.example.netcloudmusic.core.design.theme.SpaceMediumHeight
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.me_fans
import netcloudmusic.shared.generated.resources.me_friend
import org.jetbrains.compose.resources.stringResource

@Composable
fun CountInfo(
    followingsCount: Long = 0,
    followersCount: Long = 0,
    commentsCount: Long = 0,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CountInfoItem(
                count = followingsCount.toString(),
                label = stringResource(Res.string.me_friend),
                modifier = Modifier.weight(1f),
            )
            CountInfoDivider()
            CountInfoItem(
                count = followersCount.toString(),
                label = stringResource(Res.string.me_fans),
                modifier = Modifier.weight(1f),
            )
            CountInfoDivider()
            CountInfoItem(count = "0", label = "收藏", modifier = Modifier.weight(1f))
            CountInfoDivider()
            CountInfoItem(
                count = commentsCount.toString(),
                label = "评价",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun CountInfoItem(count: String, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 20.dp),
    ) {
        Text(count, style = MaterialTheme.typography.titleMedium)
        SpaceMediumHeight()
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun CountInfoDivider() {
    Spacer(
        Modifier
            .height(SpaceLarge)
            .width(SpaceExtraSmall)
            .background(LocalDividerColor.current),
    )
}
