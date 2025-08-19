package com.bottari.presentation.view.checklist.personal

import com.bottari.presentation.model.ChecklistItemUiModel

data class ChecklistUiState(
    val isLoading: Boolean = false,
    val initialItems: List<ChecklistItemUiModel> = emptyList(),
    val bottariItems: List<ChecklistItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
) {
    private val nonCheckedItems: List<ChecklistItemUiModel> =
        bottariItems.filterNot { it.isChecked }
    val totalQuantity: Int = bottariItems.size
    val checkedQuantity: Int = bottariItems.count { it.isChecked }
    val nonSwipedItems: List<ChecklistItemUiModel> =
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    val isItemsEmpty: Boolean = bottariItems.isEmpty()
    val isAllChecked: Boolean = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()
    val isAllSwiped: Boolean = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()
    val isDone: Boolean = isAllSwiped || isItemsEmpty
    val isAnyChecked: Boolean = bottariItems.any { it.isChecked }
}
