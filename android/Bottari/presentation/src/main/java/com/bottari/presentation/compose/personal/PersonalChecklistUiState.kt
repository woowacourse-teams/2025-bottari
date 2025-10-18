package com.bottari.presentation.compose.personal

import com.bottari.presentation.model.bottari.ChecklistItemUiModel

data class PersonalChecklistUiState(
    val isLoading: Boolean = false,
    val initialItems: List<ChecklistItemUiModel> = emptyList(),
    val bottariItems: List<ChecklistItemUiModel> = emptyList(),
    val swipedItemIds: Set<Long> = emptySet(),
    val isTooltipClosed: Boolean = true,
) {
    val nonCheckedItems: List<ChecklistItemUiModel> =
        bottariItems.filterNot { it.isChecked }
    val totalQuantity: Int = bottariItems.size
    val checkedQuantity: Int = bottariItems.count { it.isChecked }
    val isCompleted: Boolean = checkedQuantity == totalQuantity
    val isAnyChecked: Boolean = bottariItems.any { it.isChecked }
}
