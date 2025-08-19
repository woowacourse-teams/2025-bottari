package com.bottari.presentation.view.checklist.team.main.checklist

import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<TeamChecklistProductUiModel> = emptyList(),
    val expandableItems: List<TeamChecklistItem> = emptyList(),
    val swipedItems: List<TeamChecklistItem> = emptyList(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val checkedQuantity: Int
        get() = bottariItems.count { it.isChecked }

    val nonSwipedItems: List<TeamChecklistProductUiModel>
        get() = bottariItems.filterNot { it.isChecked || it in swipedItems }

    val isItemsEmpty: Boolean
        get() = bottariItems.isEmpty()

    val isAllChecked: Boolean
        get() = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean
        get() = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()

    private val nonCheckedItems: List<TeamChecklistProductUiModel>
        get() = bottariItems.filterNot { it.isChecked }
}
