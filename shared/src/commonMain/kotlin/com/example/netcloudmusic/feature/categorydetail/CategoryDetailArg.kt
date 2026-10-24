package com.example.netcloudmusic.feature.categorydetail

import kotlinx.serialization.Serializable

/**
 * 三级子分类详情页的入参
 *
 * @param id         三级子分类自身 id（如 "410010"）
 * @param title      三级子分类名称（如 "K系列"）
 * @param icon       三级子分类图标 URL
 * @param parentId   三级子分类的 parentId（即二级分类 id，如 "41000"）
 * @param sort       排序值
 * @param firstId    一级分类 id（左侧列表当前选中，如 "4000"）
 * @param secondId   二级分类 id（右侧所属分组标题，如 "40000"）
 * @param firstName  一级分类名称（如 "手机数码"）
 * @param secondName 二级分类名称（如 "小米系列"）
 */
@Serializable
data class CategoryDetailArg(
    val id: String,
    val title: String,
    val icon: String? = null,
    val parentId: String? = null,
    val sort: Long? = null,
    val firstId: String,
    val secondId: String,
    val firstName: String = "",
    val secondName: String = "",
)
