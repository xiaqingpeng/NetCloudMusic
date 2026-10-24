package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.Category
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 分类数据仓库
 */
class CategoryRepository(
    private val network: NetworkDataSource,
) {
    /**
     * 获取分类列表
     * @param parent 父分类 ID，为空则获取一级分类
     */
    fun categories(parent: String? = null): Flow<NetworkResponse<NetworkPageData<Category>>> =
        flow {
            emit(network.categories(parent))
        }.flowOn(Dispatchers.IO)
}
