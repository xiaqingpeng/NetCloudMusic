package com.example.netcloudmusic.feature.discovery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.discovery_screen
import org.jetbrains.compose.resources.stringResource

/**
 * 发现界面
 */
@Composable
fun DiscoveryRoute() {
    DiscoveryScreen()
}

@Composable
fun DiscoveryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(Res.string.discovery_screen))
    }
}
