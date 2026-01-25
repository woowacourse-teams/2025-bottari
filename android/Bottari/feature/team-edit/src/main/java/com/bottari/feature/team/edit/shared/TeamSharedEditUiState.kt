package com.bottari.feature.team.edit.shared

import com.bottari.core.ui.model.bottari.BottariItemUiModel

data class TeamSharedEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val sharedItems: List<BottariItemUiModel> = emptyList(),
    val inputText: String = "",
) {
    val isEmpty: Boolean = isFetched && sharedItems.isEmpty()
    val isAlreadyExist: Boolean = sharedItems.any { it.name == inputText }
    val isSavable: Boolean = inputText.isNotBlank() && !isAlreadyExist
}
