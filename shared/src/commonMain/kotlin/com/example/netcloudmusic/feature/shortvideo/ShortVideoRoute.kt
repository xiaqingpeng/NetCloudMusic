package com.example.netcloudmusic.feature.shortvideo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("短视频界面")
    }
}
