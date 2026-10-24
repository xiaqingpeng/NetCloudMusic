package com.example.netcloudmusic.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.CategoryRepository
import com.example.netcloudmusic.core.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 分类界面 ViewModel
 */
class CategoryViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _leftDatum = MutableStateFlow<List<Category>>(emptyList())
    val leftDatum: StateFlow<List<Category>> = _leftDatum

    private val _rightDatum = MutableStateFlow<List<Category>>(emptyList())
    val rightDatum: StateFlow<List<Category>> = _rightDatum

    private val _leftSelectedId = MutableStateFlow<String>("")
    val leftSelectedId: StateFlow<String> = _leftSelectedId

    init {
        loadLeftData()
    }

    private fun loadLeftData() {
        viewModelScope.launch {
            try {
                println("CategoryViewModel: 开始加载左侧分类...")
                categoryRepository.categories().collect { response ->
                    println("CategoryViewModel: 收到响应 - isSucceeded=${response.isSucceeded}, status=${response.status}, message=${response.message}")
                    println("CategoryViewModel: data=${response.data}, list size=${response.data?.list?.size}")
                    if (response.isSucceeded) {
                        _leftDatum.value = response.data?.list ?: emptyList()
                        println("CategoryViewModel: 左侧分类数量: ${_leftDatum.value.size}")
                        // 自动加载第一个分类的子分类
                        _leftDatum.value.firstOrNull()?.let {
                            println("CategoryViewModel: 自动加载第一个分类的子分类: ${it.title} (${it.id})")
                            loadRightData(it.id)
                        }
                    } else {
                        // TODO: 错误处理
                        println("CategoryViewModel: 加载左侧分类失败: ${response.message}, status=${response.status}")
                    }
                }
            } catch (e: Exception) {
                println("CategoryViewModel: 加载左侧分类异常: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun loadRightData(parentId: String) {
        _leftSelectedId.value = parentId

        viewModelScope.launch {
            try {
                println("CategoryViewModel: 开始加载右侧分类，parentId=$parentId")
                categoryRepository.categories(parent = parentId).collect { response ->
                    println("CategoryViewModel: 右侧分类响应 - isSucceeded=${response.isSucceeded}, status=${response.status}, message=${response.message}")
                    println("CategoryViewModel: data=${response.data}, list size=${response.data?.list?.size}")
                    if (response.isSucceeded) {
                        _rightDatum.value = response.data?.list ?: emptyList()
                        println("CategoryViewModel: 右侧分类数量: ${_rightDatum.value.size}")
                    } else {
                        // TODO: 错误处理
                        println("CategoryViewModel: 加载右侧分类失败: ${response.message}, status=${response.status}")
                    }
                }
            } catch (e: Exception) {
                println("CategoryViewModel: 加载右侧分类异常: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
