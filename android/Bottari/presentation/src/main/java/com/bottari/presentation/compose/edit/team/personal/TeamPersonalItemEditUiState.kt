package com.bottari.presentation.compose.edit.team.personal

import com.bottari.presentation.model.bottari.BottariItemUiModel

data class TeamPersonalItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && personalItems.isEmpty()
    val isAlreadyExist: Boolean = personalItems.any { it.name == inputText }
}
