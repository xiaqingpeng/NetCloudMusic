package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.core.model.Address
import com.example.netcloudmusic.core.model.BaseId
import com.example.netcloudmusic.core.model.BaseModel
import com.example.netcloudmusic.core.model.response.NetworkPageData
import com.example.netcloudmusic.core.model.response.NetworkResponse
import com.example.netcloudmusic.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 收货地址数据仓库
 */
class AddressRepository(
    private val dataSource: NetworkDataSource,
) {
    fun getAddressList(): Flow<NetworkResponse<NetworkPageData<Address>>> = flow {
        emit(dataSource.getAddressList())
    }

    fun addressDetail(id: String): Flow<NetworkResponse<Address>> = flow {
        emit(dataSource.addressDetail(id))
    }

    fun createAddress(data: Address): Flow<NetworkResponse<BaseId>> = flow {
        emit(dataSource.createAddress(data))
    }

    fun updateAddress(data: Address): Flow<NetworkResponse<BaseId>> = flow {
        emit(dataSource.updateAddress(data))
    }

    fun deleteAddress(id: String): Flow<NetworkResponse<BaseModel>> = flow {
        emit(dataSource.deleteAddress(id))
    }
}
