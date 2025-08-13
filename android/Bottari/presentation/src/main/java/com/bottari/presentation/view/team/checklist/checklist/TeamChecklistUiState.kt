package com.bottari.presentation.view.team.checklist.checklist

import com.bottari.presentation.model.TeamBottariItemUIModel
import com.bottari.presentation.view.team.checklist.checklist.adapter.TeamChecklistItem

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val allItems: List<TeamBottariItemUIModel> = emptyList(),
    val pointItems: List<TeamBottariItemUIModel> = emptyList(),
    val personalItems: List<TeamBottariItemUIModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
    val expandableItems: List<TeamChecklistItem> = emptyList(),
) {
    private val bottariItems: List<TeamBottariItemUIModel> = allItems + pointItems + personalItems
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<TeamBottariItemUIModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<TeamBottariItemUIModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
