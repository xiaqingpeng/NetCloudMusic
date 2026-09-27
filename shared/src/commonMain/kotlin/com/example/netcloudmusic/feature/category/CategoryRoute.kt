package com.example.netcloudmusic.feature.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.category_screen
import org.jetbrains.compose.resources.stringResource

/**
 * 分类界面
 */
@Composable
fun CategoryRoute() {
    CategoryScreen()
}

@Composable
fun CategoryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(Res.string.category_screen))
    }
}
