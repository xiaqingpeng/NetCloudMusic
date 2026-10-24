package com.example.netcloudmusic.core.util

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.posix.O_CREAT
import platform.posix.O_TRUNC
import platform.posix.O_WRONLY
import platform.posix.close
import platform.posix.open
import platform.posix.write

@Composable
actual fun rememberShareLauncher(): ShareLauncher = IosShareLauncher

private object IosShareLauncher : ShareLauncher {
    override fun shareText(text: String) {
        presentShareController(listOf("推荐一个好商品：$text"))
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun shareImage(data: ByteArray) {
        val cachePath = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true,
        ).firstOrNull() as? String ?: return
        val filePath = "$cachePath/share_product.jpg"
        val descriptor = open(filePath, O_WRONLY or O_CREAT or O_TRUNC, 0x180)
        if (descriptor < 0) return
        data.usePinned { pinned ->
            var offset = 0
            while (offset < data.size) {
                val written = write(
                    descriptor,
                    pinned.addressOf(offset),
                    (data.size - offset).toULong(),
                ).toInt()
                if (written <= 0) return@usePinned
                offset += written
            }
        }
        close(descriptor)
        presentShareController(listOf(platform.Foundation.NSURL.fileURLWithPath(filePath)))
    }

    private fun presentShareController(items: List<Any>) {
        val controller = UIActivityViewController(
            activityItems = items,
            applicationActivities = null,
        )
        topViewController()?.presentViewController(
            viewControllerToPresent = controller,
            animated = true,
            completion = null,
        )
    }

    private fun topViewController(): UIViewController? {
        val activeScene = UIApplication.sharedApplication.connectedScenes
            .firstOrNull { it is UIWindowScene } as? UIWindowScene
        val sceneWindow = activeScene?.windows
            ?.filterIsInstance<UIWindow>()
            ?.firstOrNull { it.isKeyWindow() }
        val root = (
            UIApplication.sharedApplication.keyWindow
                ?: sceneWindow
        )?.rootViewController
            ?: return null
        var current = root
        while (true) {
            val presented = current.presentedViewController ?: break
            current = presented
        }
        return current
    }
}
