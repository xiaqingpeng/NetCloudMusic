package com.example.netcloudmusic.feature.selectcity

/** 城市条目（对应 Compose 项目的 CityEntity，KMP 版不依赖 Room） */
data class CityItem(
    val code: String,
    val name: String,
    val parentCode: String? = null,
    /** 0=省 1=市 2=区 */
    val type: Int = 0,
)

sealed interface SelectCityUiState {
    data object None : SelectCityUiState
    data object Loading : SelectCityUiState
    data class Success(val items: List<CityItem>) : SelectCityUiState
    data class Error(val message: String? = null) : SelectCityUiState
}
