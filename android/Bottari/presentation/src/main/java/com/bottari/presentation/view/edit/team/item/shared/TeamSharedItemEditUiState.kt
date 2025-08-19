package com.bottari.presentation.view.edit.team.item.shared

import com.bottari.presentation.model.BottariItemUiModel

data class TeamSharedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val sharedItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && sharedItems.isEmpty()
    val isAlreadyExist: Boolean = sharedItems.any { it.name == inputText }
}
