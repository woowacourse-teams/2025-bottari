package com.bottari.presentation.view.edit.team.item.shared

import com.bottari.presentation.model.BottariItemUiModel

data class TeamSharedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && personalItems.isEmpty()
    val isAlreadyExist: Boolean = personalItems.any { it.name == inputText }
}
