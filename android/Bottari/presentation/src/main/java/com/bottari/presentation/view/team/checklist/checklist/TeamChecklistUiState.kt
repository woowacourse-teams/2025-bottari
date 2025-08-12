package com.bottari.presentation.view.team.checklist.checklist

import com.bottari.presentation.model.BottariItemUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val allItems: List<BottariItemUiModel> = emptyList(),
    val pointItems: List<BottariItemUiModel> = emptyList(),
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
    val expandableItems: List<Any> = emptyList(),
) {
    private val bottariItems: List<BottariItemUiModel> = allItems + pointItems + personalItems
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<BottariItemUiModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<BottariItemUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
