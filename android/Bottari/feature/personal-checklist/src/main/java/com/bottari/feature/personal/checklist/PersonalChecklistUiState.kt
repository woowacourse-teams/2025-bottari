package com.bottari.feature.personal.checklist

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel

@Immutable
data class PersonalChecklistUiState(
    val isLoading: Boolean = false,
    val initialItems: List<PersonalChecklistItemUiModel> = emptyList(),
    val bottariItems: List<PersonalChecklistItemUiModel> = emptyList(),
    val isTooltipClosed: Boolean = true,
) {
    val nonCheckedItems: List<PersonalChecklistItemUiModel> =
        bottariItems.filterNot { it.isChecked }
    val totalQuantity: Int = bottariItems.size
    val checkedQuantity: Int = bottariItems.count { it.isChecked }
    val isCompleted: Boolean = checkedQuantity == totalQuantity
    val isAnyChecked: Boolean = bottariItems.any { it.isChecked }
    val isItemsEmpty: Boolean = bottariItems.isEmpty()
}
