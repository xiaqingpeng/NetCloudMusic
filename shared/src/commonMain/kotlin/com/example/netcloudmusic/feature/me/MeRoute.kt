package com.example.netcloudmusic.feature.me

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.me_screen
import org.jetbrains.compose.resources.stringResource

/**
 * 我的界面
 */
@Composable
fun MeRoute() {
    MeScreen()
}

@Composable
fun MeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant) ,
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(Res.string.me_screen))
    }
}
