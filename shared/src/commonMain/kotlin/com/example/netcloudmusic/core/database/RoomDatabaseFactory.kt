package com.example.netcloudmusic.core.database

import androidx.room3.RoomDatabase

/**
 * 跨平台 Room 数据库构建工厂。
 *
 * 各平台负责解析可写的数据库绝对路径、复制预填数据库，并返回
 * 已配好路径的 RoomDatabase.Builder。driver 在 common 代码统一设置。
 *
 * Android: 用 Context.getDatabasePath 的绝对路径。
 * iOS: 用 NSDocumentDirectory 下的绝对路径。
 */
expect class RoomDatabaseFactory(context: Any?) {
    /**
     * 创建已配置好数据库路径的 RoomDatabase.Builder，并复制预填数据库（若需要）。
     * @param dbName 数据库文件名，如 "city.db"
     */
    fun createBuilder(dbName: String): RoomDatabase.Builder<CityDatabase>
}
