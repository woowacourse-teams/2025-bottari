package com.bottari.presentation.view.checklist

import com.bottari.presentation.model.BottariItemUiModel

data class ChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<BottariItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<BottariItemUiModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<BottariItemUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
