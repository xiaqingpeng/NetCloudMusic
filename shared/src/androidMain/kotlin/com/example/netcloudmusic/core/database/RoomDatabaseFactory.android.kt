package com.example.netcloudmusic.core.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

/**
 * Android 平台 Room 数据库构建工厂。
 *
 * 用 Context.getDatabasePath 解析可写的绝对路径，从 assets 复制预填数据库，
 * 返回已配好路径的 RoomDatabase.Builder。
 */
actual class RoomDatabaseFactory actual constructor(context: Any?) {
    private val androidContext: Context = (context as Context).applicationContext

    actual fun createBuilder(dbName: String): RoomDatabase.Builder<CityDatabase> {
        // 数据库必须放在应用私有可写目录（getDatabasePath），不能用相对路径，否则会落到只读文件系统
        val dbFile = androidContext.getDatabasePath(dbName)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            try {
                androidContext.assets.open(dbName).use { input ->
                    dbFile.outputStream().use { output -> input.copyTo(output) }
                }
            } catch (e: Exception) {
                // assets 中没有预填数据库，Room 会自动创建空数据库
                // 后续可通过网络 API 加载城市数据并插入
            }
        }
        return Room.databaseBuilder<CityDatabase>(
            context = androidContext,
            name = dbFile.absolutePath,
        )
    }
}
