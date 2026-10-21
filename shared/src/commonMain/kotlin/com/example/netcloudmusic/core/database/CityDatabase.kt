package com.example.netcloudmusic.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.netcloudmusic.core.database.dao.CityDao
import com.example.netcloudmusic.core.database.model.CityEntity

/**
 * 城市数据库（KMP 版 Room 3）。
 *
 * @ConstructedBy 让 Room 编译器自动生成各平台的 actual 实现，
 * 不需要手动写 expect/actual RoomDatabase 构造器。
 */
@Database(
    entities = [CityEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(CityDatabaseConstructor::class)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}

/**
 * Room 编译器会自动在 commonMain 生成 expect，各平台生成 actual。
 * 这个 interface 只需声明，不需要手写实现。
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "KotlinNoActualForExpect")
expect object CityDatabaseConstructor : RoomDatabaseConstructor<CityDatabase>
