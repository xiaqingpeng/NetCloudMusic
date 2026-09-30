package com.example.netcloudmusic.feature.shortvideo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.short_video_screen
import org.jetbrains.compose.resources.stringResource

/**
 * 短视频界面
 */
@Composable
fun ShortVideoRoute() {
    ShortVideoScreen()
}

@Composable
fun ShortVideoScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant) ,
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(Res.string.short_video_screen))
    }
}
