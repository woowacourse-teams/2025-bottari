package com.bottari.presentation.view.edit.personal.item

import com.bottari.presentation.model.BottariItemUiModel

data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val id: Long,
    val title: String,
    var items: List<BottariItemUiModel>,
)
