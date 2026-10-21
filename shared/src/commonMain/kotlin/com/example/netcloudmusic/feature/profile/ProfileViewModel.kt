package com.example.netcloudmusic.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _data = MutableStateFlow(User())
    val data: StateFlow<User> = _data.asStateFlow()

    private val _isShowGenderDialog = MutableStateFlow(false)
    val isShowGenderDialog: StateFlow<Boolean> = _isShowGenderDialog.asStateFlow()

    private val _isShowConfirmLogoutDialog = MutableStateFlow(false)
    val isShowConfirmLogoutDialog: StateFlow<Boolean> = _isShowConfirmLogoutDialog.asStateFlow()

    private val _toast = MutableStateFlow<ProfileToastEvent?>(null)
    val toast: StateFlow<ProfileToastEvent?> = _toast.asStateFlow()

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack.asStateFlow()

    init {
        // 先用本地缓存填充界面，让用户立刻看到数据
        userRepository.getLocalUser()?.let { local ->
            _data.value = local
            _uiState.update { ProfileUiState.Success }
        }
        // 再从网络拉最新数据
        loadFromNetwork()
    }

    private fun loadFromNetwork() {
        val userId = userRepository.getLocalUser()?.id ?: run {
            if (_uiState.value == ProfileUiState.Loading) {
                _uiState.update { ProfileUiState.Error("未登录，请先登录") }
            }
            return
        }
        viewModelScope.launch {
            try {
                userRepository.userDetail(userId).collect { response ->
                    if (response.isSucceeded && response.data != null) {
                        _data.value = response.data
                        _uiState.update { ProfileUiState.Success }
                    } else {
                        // 网络失败时，若已有本地数据就继续显示，否则报错
                        if (_data.value.id == null) {
                            _uiState.update { ProfileUiState.Error(response.message) }
                        }
                    }
                }
            } catch (e: Exception) {
                if (_data.value.id == null) {
                    _uiState.update { ProfileUiState.Error(e.message) }
                }
            }
        }
    }

    fun onUserValueChanged(user: User) { _data.value = user }

    /** 从城市选择页返回后更新省市区字段 */
    fun onCitySelected(
        province: String, provinceCode: String,
        city: String, cityCode: String,
        area: String, areaCode: String,
    ) {
        _data.value = _data.value.copy(
            province = province, provinceCode = provinceCode,
            city = city, cityCode = cityCode,
            area = area, areaCode = areaCode,
        )
    }

    fun onSaveClick() {
        if (_uiState.value == ProfileUiState.Saving) return   // 防重复提交
        viewModelScope.launch {
            _uiState.update { ProfileUiState.Saving }
            try {
                userRepository.updateUser(_data.value).collect { response ->
                    if (response.isSucceeded) {
                        _toast.value = ProfileToastEvent.Text("保存成功")
                        _uiState.update { ProfileUiState.Success }
                        _navigateBack.value = true
                    } else {
                        _uiState.update { ProfileUiState.Success }
                        _toast.value = ProfileToastEvent.Text(response.message ?: "保存失败")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { ProfileUiState.Success }
                _toast.value = ProfileToastEvent.Text(e.message ?: "保存失败")
            }
        }
    }

    fun consumeToast() { _toast.value = null }
    fun consumeNavigateBack() { _navigateBack.value = false }

    fun showGenderDialog() { _isShowGenderDialog.value = true }
    fun dismissGenderDialog() { _isShowGenderDialog.value = false }
    fun showConfirmLogoutDialog() { _isShowConfirmLogoutDialog.value = true }
    fun dismissConfirmLogoutDialog() { _isShowConfirmLogoutDialog.value = false }

    fun confirmLogout() {
        dismissConfirmLogoutDialog()
        userRepository.logout()
        _navigateBack.value = true
    }
}
