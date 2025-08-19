package com.bottari.presentation.view.edit.personal.item

import com.bottari.presentation.model.BottariItemUiModel

data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val bottariId: Long,
    val title: String,
    var originalItems: List<BottariItemUiModel>,
    var items: List<BottariItemUiModel>,
) {
    val isNotEqual: Boolean = originalItems != items
}
