package com.example.netcloudmusic.core.util

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

@Composable
actual fun rememberShareLauncher(): ShareLauncher {
    val context = LocalContext.current
    return ShareLauncherImpl(context)
}

private class ShareLauncherImpl(
    private val context: Context,
) : ShareLauncher {
    override fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "推荐一个好商品：$text")
        }
        context.startActivity(Intent.createChooser(intent, null))
    }

    override fun shareImage(data: ByteArray) {
        val imageFormat = detectImageFormat(data)
        val file = File(context.cacheDir, "share_product_${UUID.randomUUID()}${imageFormat.extension}").apply {
            writeBytes(data)
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.app_provider",
            file,
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = imageFormat.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "分享商品图片"))
    }

    private fun detectImageFormat(data: ByteArray): ImageFormat {
        return when {
            data.startsWith(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)) ->
                ImageFormat(".png", "image/png")
            data.startsWith(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())) ->
                ImageFormat(".jpg", "image/jpeg")
            data.startsWith("RIFF".encodeToByteArray()) &&
                data.size >= 12 &&
                data.copyOfRange(8, 12).contentEquals("WEBP".encodeToByteArray()) ->
                ImageFormat(".webp", "image/webp")
            else -> ImageFormat(".jpg", "image/jpeg")
        }
    }

    private data class ImageFormat(
        val extension: String,
        val mimeType: String,
    )

    private fun ByteArray.startsWith(prefix: ByteArray): Boolean =
        size >= prefix.size && prefix.indices.all { this[it] == prefix[it] }
}
