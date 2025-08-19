package com.bottari.presentation.view.checklist.personal

import com.bottari.presentation.model.ChecklistItemUiModel

data class ChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<ChecklistItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<ChecklistItemUiModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    val isDone: Boolean
        get() = isAllSwiped || isAllChecked

    private val nonCheckedItems: List<ChecklistItemUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
