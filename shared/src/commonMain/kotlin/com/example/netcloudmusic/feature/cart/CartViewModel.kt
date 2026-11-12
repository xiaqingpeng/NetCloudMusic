package com.example.netcloudmusic.feature.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netcloudmusic.core.data.repository.CartRepository
import com.example.netcloudmusic.core.model.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {

    // 购物车列表
    private val _datum = MutableStateFlow<List<Cart>>(emptyList())
    val datum: StateFlow<List<Cart>> = _datum

    // 加载状态
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 是否全选
    val isCheckAll: StateFlow<Boolean> = _datum
        .map { list -> list.isNotEmpty() && list.all { it.selected } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    // 已选商品数量
    val selectedCount: StateFlow<Int> = _datum
        .map { list -> list.count { it.selected } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0
        )

    // 已选商品总价（分）
    val price: StateFlow<Int> = _datum
        .map { list -> list.sumOf { if (it.selected) it.product!!.price * it.count else 0 } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0
        )

    // 是否进入编辑模式
    val isEditing = MutableStateFlow(false)

    var shouldDisplayUndoRemove by mutableStateOf(false)
    private var lastRemoveCartIds: List<String>? = null
    private var lastRemovedCartSnapshot: List<Cart>? = null

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.cartsSync()
                if (response.isSucceeded) {
                    _datum.value = response.data?.list
                        ?.map { it.copy(selected = false) }
                        ?: emptyList()
                }
            } catch (e: Exception) {
                // 失败时保持当前列表不变
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun itemCheckChanged(data: Cart) {
        _datum.value = _datum.value.map { cart ->
            if (cart.id == data.id) {
                cart.copy(selected = !cart.selected)
            } else {
                cart
            }
        }
    }

    fun onDecrementCount(data: Cart) {
        if (data.count > 0) editCart(data.copy(count = data.count - 1))
    }

    fun onIncrementCount(data: Cart) {
        if (data.count < 100) editCart(data.copy(count = data.count + 1))
    }

    private fun editCart(data: Cart) {
        viewModelScope.launch {
            try {
                val response = cartRepository.addProductToCartSync(data.copy(update = true))
                if (response.isSucceeded) loadData()
            } catch (e: Exception) {
                // 失败时保持当前状态
            }
        }
    }

    fun checkAllChanged() {
        val allSelected = isCheckAll.value
        _datum.value = _datum.value.map { it.copy(selected = !allSelected) }
    }

    fun onDeleteAllClick() {
        prepareDeleteByIds(selectedCartIds())
    }

    private fun prepareDeleteByIds(ids: List<String>) {
        if (ids.isEmpty() || shouldDisplayUndoRemove) return

        lastRemoveCartIds = ids
        lastRemovedCartSnapshot = _datum.value.filter { it.id in ids }
        shouldDisplayUndoRemove = true
        // 乐观更新：先从本地列表移除，等用户确认后再请求服务端
        _datum.value = _datum.value.filter { it.id !in ids }
    }

    private fun deleteByIds(ids: List<String>) {
        viewModelScope.launch {
            try {
                val response = cartRepository.deleteCartsSync(ids)
                if (response.isSucceeded) {
                    clearUndoState()
                    isEditing.value = false
                    loadData()
                } else {
                    restoreLastRemovedItems()
                }
            } catch (e: Exception) {
                restoreLastRemovedItems()
            }
        }
    }

    private fun restoreLastRemovedItems() {
        lastRemovedCartSnapshot?.let { removedItems ->
            val currentIds = _datum.value.mapNotNull { it.id }.toSet()
            _datum.value += removedItems.filter { it.id !in currentIds }
        }
        clearUndoState()
    }

    fun selectedCartIds(): List<String> =
        _datum.value.filter { it.selected }.mapNotNull { it.id }

    fun toggleEditingModel() {
        isEditing.value = !isEditing.value
    }

    fun undoCartRemoval() {
        restoreLastRemovedItems()
        isEditing.value = false
        loadData()
    }

    fun confirmDeleteByIds() {
        val ids = lastRemoveCartIds ?: return
        shouldDisplayUndoRemove = false
        deleteByIds(ids)
    }

    fun clearUndoState() {
        shouldDisplayUndoRemove = false
        lastRemoveCartIds = null
        lastRemovedCartSnapshot = null
    }
}
