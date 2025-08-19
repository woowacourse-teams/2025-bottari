package com.bottari.presentation.view.home.personal

import com.bottari.presentation.model.BottariUiModel

data class BottariUiState(
    val isLoading: Boolean = false,
    val bottaries: List<BottariUiModel> = emptyList(),
    val isFetched: Boolean = false,
) {
    val isEmpty: Boolean = isFetched && bottaries.isEmpty()
}
