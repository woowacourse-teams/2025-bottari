package com.bottari.presentation.compose.edit.team.shared

import com.bottari.presentation.model.bottari.BottariItemUiModel

data class TeamSharedItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val sharedItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && sharedItems.isEmpty()
    val isAlreadyExist: Boolean = sharedItems.any { it.name == inputText }
}
