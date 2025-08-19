package com.bottari.presentation.view.checklist.team.main.checklist

import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel

data class TeamChecklistUiState(
    val isLoading: Boolean = false,
    val originalBottariItems: List<TeamChecklistProductUiModel> = emptyList(),
    val bottariItems: List<TeamChecklistProductUiModel> = emptyList(),
    val expandableItems: List<TeamChecklistItem> = emptyList(),
    val swipedItems: List<TeamChecklistItem> = emptyList(),
) {
    val totalQuantity: Int = bottariItems.size

    val checkedQuantity: Int = bottariItems.count { it.isChecked }

    val unCheckedQuantity: Int = totalQuantity - checkedQuantity

    val nonSwipedItems: List<TeamChecklistProductUiModel> by lazy {
        nonCheckedItems.filterNot { it in swipedItems }
    }

    val isItemsEmpty: Boolean = bottariItems.isEmpty()

    private val nonCheckedItems: List<TeamChecklistProductUiModel> by lazy {
        bottariItems.filterNot { it.isChecked }
    }

    val isAllChecked: Boolean = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()

    val isAllSwiped: Boolean = bottariItems.isNotEmpty() && nonSwipedItems.isEmpty()
}
