package com.example.netcloudmusic.feature.selectcity

/**
 * 城市选择结果回调桥。
 *
 * 由于 Compose Navigation 跨页面 lambda 无法序列化，
 * 使用单例暂存「addressdetail → selectcity」的结果回调。
 * 回调在 selectcity 页选完城市后触发，然后立即清空。
 */
object SelectCityCallback {
    var onResult: ((province: String, provinceCode: String,
                    city: String, cityCode: String,
                    area: String, areaCode: String) -> Unit)? = null
}
