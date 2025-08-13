package com.bottari.presentation.view.team.checklist.checklist

import com.bottari.presentation.model.TeamBottariItemUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val allItems: List<TeamBottariItemUiModel> = emptyList(),
    val pointItems: List<TeamBottariItemUiModel> = emptyList(),
    val personalItems: List<TeamBottariItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
    val expandableItems: List<Any> = emptyList(),
) {
    private val bottariItems: List<TeamBottariItemUiModel> = allItems + pointItems + personalItems
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<TeamBottariItemUiModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<TeamBottariItemUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
