package com.bottari.presentation.view.edit.personal.item

import com.bottari.presentation.model.BottariItemUiModel

data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val bottariId: Long,
    val title: String,
    var items: List<BottariItemUiModel>,
) {
    val initialItems: List<String> = items.map { it.name }
    val initialItemIds: List<Long> = items.map { it.id }
}
