package com.bottari.presentation.view.edit.personal.item

import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long = 0,
    val title: String = "",
    var items: List<PersonalChecklistItemUiModel> = emptyList(),
) {
    val isEmpty: Boolean = !isLoading && isFetched && items.isEmpty()
}
