package com.bottari.presentation.compose.edit.personal.item

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

@Immutable
data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long = 0,
    val title: String = "",
    val itemName: String = "",
    var items: List<ChecklistItemUiModel> = emptyList(),
) {
    val isEmpty: Boolean = !isLoading && isFetched && items.isEmpty()
    val isDuplicate: Boolean = isEmpty.not() && items.any { item -> item.name == itemName }
}
