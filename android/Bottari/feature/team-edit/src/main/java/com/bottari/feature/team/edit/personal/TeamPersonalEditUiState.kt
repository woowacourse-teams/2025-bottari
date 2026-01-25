package com.bottari.feature.team.edit.personal

import com.bottari.core.ui.model.bottari.BottariItemUiModel

data class TeamPersonalEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val personalItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && personalItems.isEmpty()
    val isAlreadyExist: Boolean = personalItems.any { it.name == inputText }
    val isSavable: Boolean = inputText.isNotBlank() && !isAlreadyExist
}
