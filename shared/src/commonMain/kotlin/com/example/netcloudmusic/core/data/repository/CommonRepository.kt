package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.ViewData
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource
import com.example.netcloudmusic.core.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 通用数据仓库
 * 
 * 提供首页区块数据（Banner、功能按钮、大图、热门商品等）
 */
class CommonRepository(
    private val network: NetworkDataSource,
) {
    /**
     * 首页数据（Banner、功能按钮、大图、热门商品等）
     */
    suspend fun indexes(
        app: Int = Constant.VALUE0,
    ): NetworkResponse<NetworkPageData<ViewData>> {
        return network.indexes(app = app)
    }

    fun indexesFlow(
        app: Int = Constant.VALUE0,
    ): Flow<NetworkResponse<NetworkPageData<ViewData>>> = flow {
        emit(network.indexes(app = app))
    }.flowOn(Dispatchers.IO)
}
