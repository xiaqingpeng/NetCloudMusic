package com.example.netcloudmusic.core.util

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@androidx.compose.runtime.Composable
actual fun rememberExternalUrlOpener(): (String) -> Unit {
    val context = LocalContext.current
    return { url ->
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
