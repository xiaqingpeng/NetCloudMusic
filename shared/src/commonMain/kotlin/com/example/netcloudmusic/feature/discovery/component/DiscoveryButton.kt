package com.example.netcloudmusic.feature.discovery.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.SpaceExtraSmall2
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.model.ButtonViewData
import com.example.netcloudmusic.core.util.ResourceUtil

/**
 * 功能按钮区
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoveryButton(data: List<ButtonViewData>) {
    if (data.isEmpty()) return
    
    Card {
        FlowRow(
            maxItemsInEachRow = 5,
            modifier = Modifier.fillMaxWidth(),
        ) {
            data.forEach {
                DiscoveryButtonItem(
                    data = it,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun DiscoveryButtonItem(
    data: ButtonViewData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(bottom = SpaceMedium, top = SpaceExtraSmall2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            model = ResourceUtil.r(data.icon),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = data.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
