package com.example.netcloudmusic.core.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * 地区实体（与 Compose 版完全一致）
 */
@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val pinyin: String,
    @ColumnInfo(name = "first_letter")
    val firstLetter: String,
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    val type: Int = 0,
    @ColumnInfo(name = "parent_code")
    val parentCode: String? = null,
)
