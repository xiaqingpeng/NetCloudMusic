package com.example.netcloudmusic.feature.guide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * 引导界面
 */
@Composable
fun GuideRoute(
    toBack: () -> Unit,
    toMain: () -> Unit = {}
) {
    GuideScreen(
        toBack = toBack,
        toMain = toMain
    )
}

@Composable
fun GuideScreen(
    toBack: () -> Unit = {},
    toMain: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "引导界面",
            modifier = Modifier.clickable { toMain() }
        )
    }
}
