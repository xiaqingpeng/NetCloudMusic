package com.example.netcloudmusic.core.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

/**
 * iOS 平台 Room 数据库构建工厂。
 *
 * 用 NSDocumentDirectory 解析可写的绝对路径，从 App Bundle 复制预填数据库，
 * 返回已配好路径的 RoomDatabase.Builder。
 *
 * 复制策略：
 * - 目标文件不存在 → 直接复制
 * - 目标文件存在但比 Bundle 源文件小（空库或截断） → 删除后重新复制
 * - 目标文件大小 >= Bundle 源文件 → 保留（已有完整数据库，避免覆盖用户数据）
 */
@OptIn(ExperimentalForeignApi::class)
actual class RoomDatabaseFactory actual constructor(context: Any?) {
    actual fun createBuilder(dbName: String): RoomDatabase.Builder<CityDatabase> {
        val dbPath = documentDirectory() + "/$dbName"

        val resourceName = dbName.substringBeforeLast(".")
        val resourceType = dbName.substringAfterLast(".")
        val bundlePath = NSBundle.mainBundle.pathForResource(resourceName, resourceType)

        if (bundlePath != null) {
            val fileManager = NSFileManager.defaultManager
            val needsCopy = if (!fileManager.fileExistsAtPath(dbPath)) {
                // 目标不存在，需要复制
                true
            } else {
                // 目标存在，比较文件大小：若目标比 Bundle 源小，说明是空库，需要重新复制
                val destSize = (fileManager.attributesOfItemAtPath(dbPath, error = null)
                    ?.get("NSFileSize") as? Long) ?: 0L
                val srcSize = (fileManager.attributesOfItemAtPath(bundlePath, error = null)
                    ?.get("NSFileSize") as? Long) ?: 0L
                destSize < srcSize
            }

            if (needsCopy) {
                // 先删除旧文件（可能是空库），再从 Bundle 复制完整预填数据库
                if (fileManager.fileExistsAtPath(dbPath)) {
                    fileManager.removeItemAtPath(dbPath, error = null)
                }
                fileManager.copyItemAtPath(
                    srcPath = bundlePath,
                    toPath = dbPath,
                    error = null,
                )
            }
        }

        return Room.databaseBuilder<CityDatabase>(name = dbPath)
    }

    private fun documentDirectory(): String {
        val dir = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true,
        ).first() as String
        return dir
    }
}
