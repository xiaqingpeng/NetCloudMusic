package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.database.dao.CityDao
import com.example.netcloudmusic.core.database.model.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * 城市数据仓库（KMP 版，与 Compose 版逻辑完全一致）
 */
class CityRepository(
    private val cityDao: CityDao,
) {
    /** 查询所有省份（第一级） */
    fun findAllByParentCodeIsNull(): Flow<List<CityEntity>> =
        cityDao.findAllByParentCodeIsNull()

    /** 查询子级城市 */
    fun findAllByParentId(parentId: String): Flow<List<CityEntity>> =
        cityDao.findAllByParentCode(parentCode = parentId)

    /** 同步查询子级城市（用于合并区县） */
    suspend fun findAllByParentIdSync(parentId: String): List<CityEntity> =
        cityDao.findAllByParentCodeSync(parentCode = parentId)
}
