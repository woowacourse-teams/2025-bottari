package com.bottari.presentation.view.checklist

import com.bottari.presentation.model.BottariItemUiModel

data class ChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<BottariItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val nonCheckedItems: List<BottariItemUiModel>
        get() = bottariItems.filter { item -> !item.isChecked }

    val checkedQuantity: Int
        get() = bottariItems.count { item -> item.isChecked }

    val nonSwipedItems: List<BottariItemUiModel>
        get() = nonCheckedItems.filter { it.id !in swipedItemIds }

    val isAllSwiped: Boolean
        get() = nonSwipedItems.isEmpty()

    val isAllChecked: Boolean
        get() = nonCheckedItems.isEmpty()
}
