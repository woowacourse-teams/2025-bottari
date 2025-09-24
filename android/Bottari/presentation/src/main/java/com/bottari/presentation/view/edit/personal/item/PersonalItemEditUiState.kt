package com.bottari.presentation.view.edit.personal.item

import com.bottari.presentation.model.bottari.ChecklistItemUiModel

data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long = 0,
    val title: String = "",
    var initialItems: List<ChecklistItemUiModel> = emptyList(),
    var items: List<ChecklistItemUiModel> = emptyList(),
) {
    val isDifferent: Boolean = initialItems != items
    val isEmpty: Boolean = isLoading && isFetched
}
