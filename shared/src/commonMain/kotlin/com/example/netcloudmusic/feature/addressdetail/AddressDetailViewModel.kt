package com.example.netcloudmusic.feature.addressdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.AddressRepository
import com.example.netcloudmusic.core.model.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** addressId = "" 表示新增模式 */
class AddressDetailViewModel(
    val addressId: String,
    private val addressRepository: AddressRepository,
) : ViewModel() {

    val isNewAddress: Boolean = addressId.isBlank()

    private val _uiState = MutableStateFlow<AddressDetailUiState>(AddressDetailUiState.None)
    val uiState: StateFlow<AddressDetailUiState> = _uiState.asStateFlow()

    private val _data = MutableStateFlow(Address())
    val data: StateFlow<Address> = _data.asStateFlow()

    init {
        if (!isNewAddress) loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                addressRepository.addressDetail(addressId).collect { response ->
                    if (response.isSucceeded && response.data != null) {
                        _data.value = response.data
                    } else {
                        _uiState.value = AddressDetailUiState.Error(response.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AddressDetailUiState.Error(e.message)
            }
        }
    }

    fun onValueChange(newData: Address) { _data.value = newData }

    /** 从城市选择页返回后更新省市区字段 */
    fun onCitySelected(province: String, provinceCode: String,
                       city: String, cityCode: String,
                       area: String, areaCode: String) {
        _data.value = _data.value.copy(
            province = province, provinceCode = provinceCode,
            city = city, cityCode = cityCode,
            area = area, areaCode = areaCode,
        )
    }

    fun onSaveClick() {
        val d = _data.value
        when {
            d.name.isBlank()      -> { _uiState.value = AddressDetailUiState.Error("请输入收货人姓名"); return }
            d.telephone.isBlank() -> { _uiState.value = AddressDetailUiState.Error("请输入手机号"); return }
            !d.telephone.matches(Regex("^1\\d{10}$")) -> { _uiState.value = AddressDetailUiState.Error("手机号格式不正确"); return }
            d.areaDisplay.isBlank() -> { _uiState.value = AddressDetailUiState.Error("请选择省市区"); return }
            d.detail.isBlank()    -> { _uiState.value = AddressDetailUiState.Error("请输入详细地址"); return }
        }
        viewModelScope.launch {
            _uiState.value = AddressDetailUiState.Loading
            try {
                val flow = if (isNewAddress) addressRepository.createAddress(d)
                           else             addressRepository.updateAddress(d)
                flow.collect { response ->
                    _uiState.value = if (response.isSucceeded) AddressDetailUiState.Success
                                     else AddressDetailUiState.Error(response.message)
                }
            } catch (e: Exception) {
                _uiState.value = AddressDetailUiState.Error(e.message)
            }
        }
    }

    fun resetUiState() { _uiState.value = AddressDetailUiState.None }
}
