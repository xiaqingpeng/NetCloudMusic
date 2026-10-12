package com.example.netcloudmusic.util

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberExternalUrlOpener(): (String) -> Unit = { url ->
    NSURL.URLWithString(url)?.let { UIApplication.sharedApplication.openURL(it) }
}
