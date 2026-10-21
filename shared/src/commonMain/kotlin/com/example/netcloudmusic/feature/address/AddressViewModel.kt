package com.example.netcloudmusic.feature.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddressViewModel(
    private val addressRepository: AddressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddressUiState>(AddressUiState.Loading)
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    init {
        loadData()
        // 监听地址保存成功信号，自动刷新列表
        AddressRefreshBus.needRefresh
            .onEach { need ->
                if (need) {
                    AddressRefreshBus.consume()
                    loadData()
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = AddressUiState.Loading
            try {
                addressRepository.getAddressList().collect { response ->
                    _uiState.value = if (response.isSucceeded) {
                        AddressUiState.Success(response.data?.list ?: emptyList())
                    } else {
                        AddressUiState.Error(response.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AddressUiState.Error(e.message)
            }
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            try {
                addressRepository.deleteAddress(id).collect { response ->
                    if (response.isSucceeded) {
                        _toast.value = "删除成功"
                        loadData()
                    } else {
                        _toast.value = response.message ?: "删除失败"
                    }
                }
            } catch (e: Exception) {
                _toast.value = e.message ?: "删除失败"
            }
        }
    }

    fun consumeToast() { _toast.value = null }
}
