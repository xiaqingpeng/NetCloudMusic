package com.example.netcloudmusic.core.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.netcloudmusic.core.database.model.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * 城市数据库访问层 DAO（与 Compose 版完全一致）
 */
@Dao
interface CityDao {
    /** 查询第一级城市（省份） */
    @Query("SELECT * FROM city WHERE parent_code IS NULL")
    fun findAllByParentCodeIsNull(): Flow<List<CityEntity>>

    /** 查询子级城市 */
    @Query("SELECT * FROM city WHERE parent_code = :parentCode")
    fun findAllByParentCode(parentCode: String): Flow<List<CityEntity>>

    /** 同步查询子级城市（用于合并区县） */
    @Query("SELECT * FROM city WHERE parent_code = :parentCode")
    suspend fun findAllByParentCodeSync(parentCode: String): List<CityEntity>
}
