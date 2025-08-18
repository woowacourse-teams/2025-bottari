package com.bottari.presentation.view.checklist.team.checklist

import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<TeamChecklistProductUiModel> = emptyList(),
    val expandableItems: List<TeamChecklistItem> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<TeamChecklistProductUiModel> by lazy {
        nonCheckedItems.filterNot { it.id in swipedItemIds }
    }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<TeamChecklistProductUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }
}
