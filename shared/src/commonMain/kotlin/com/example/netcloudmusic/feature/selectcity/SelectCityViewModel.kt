package com.example.netcloudmusic.feature.selectcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.CityRepository
import com.example.netcloudmusic.core.database.model.CityEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 城市选择 ViewModel（KMP 版）
 *
 * 数据来源：Room 数据库（通过 CityRepository），不再使用硬编码常量。
 * 用 StateFlow 持有状态，避免 mutableStateOf 被 SavedStateHandle 序列化时崩溃。
 */
class SelectCityViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val selects = ArrayDeque<CityItem>()

    private val _uiState = MutableStateFlow<SelectCityUiState>(SelectCityUiState.Loading)
    val uiState: StateFlow<SelectCityUiState> = _uiState.asStateFlow()

    init {
        loadProvinces()
    }

    /** 加载省份（parent_code 为空的第一级城市） */
    private fun loadProvinces() {
        viewModelScope.launch {
            _uiState.value = SelectCityUiState.Loading
            val provinces = cityRepository.findAllByParentCodeIsNull()
                .first()
                .map { it.toCityItem() }
            _uiState.value = SelectCityUiState.Success(provinces)
        }
    }

    /** 选中一个城市条目，返回 true 表示已选完三级 */
    fun onCitySelected(item: CityItem): Boolean {
        selects.addLast(item)

        // 已选到区级，完成
        if (item.type == 2) return true

        loadChildren(item)
        return false
    }

    /** 加载某个城市条目的子级（从数据库查询） */
    private fun loadChildren(parent: CityItem) {
        viewModelScope.launch {
            _uiState.value = SelectCityUiState.Loading
            val children = cityRepository.findAllByParentIdSync(parent.code)
                .map { it.toCityItem() }

            // 直辖市（北京/天津/上海）：省下只有一个同名市，自动跳过市级
            if (parent.type == 0 && children.size == 1 && children[0].name == parent.name) {
                selects.addLast(children[0])
                val districts = cityRepository.findAllByParentIdSync(children[0].code)
                    .map { it.toCityItem() }
                _uiState.value = SelectCityUiState.Success(districts)
                return@launch
            }

            // 重庆：省下有多个市且含同名市，合并所有区县展示
            if (parent.type == 0 && children.any { it.name == parent.name }) {
                val sameNameCity = children.first { it.name == parent.name }
                selects.addLast(sameNameCity)
                val allDistricts = children.flatMap { city ->
                    cityRepository.findAllByParentIdSync(city.code).map { it.toCityItem() }
                }
                _uiState.value = SelectCityUiState.Success(allDistricts.ifEmpty { children })
                return@launch
            }

            _uiState.value = SelectCityUiState.Success(children)
        }
    }

    /** 当前显示省级列表时才能真正返回上一页 */
    fun canBack(): Boolean = selects.isEmpty()

    /** 退回上一级城市列表 */
    fun goBack() {
        if (selects.isEmpty()) return
        selects.removeLast()
        val parentCode = selects.lastOrNull()?.code
        viewModelScope.launch {
            _uiState.value = SelectCityUiState.Loading
            val list = if (parentCode == null) {
                cityRepository.findAllByParentCodeIsNull()
                    .first()
                    .map { it.toCityItem() }
            } else {
                cityRepository.findAllByParentIdSync(parentCode).map { it.toCityItem() }
            }
            _uiState.value = SelectCityUiState.Success(list)
        }
    }

    /** 当前已选路径（省、市、区），按顺序返回 */
    fun getSelected(): List<CityItem> = selects.toList()

    private fun CityEntity.toCityItem() = CityItem(
        code = code,
        name = name,
        parentCode = parentCode,
        type = type,
    )
}
