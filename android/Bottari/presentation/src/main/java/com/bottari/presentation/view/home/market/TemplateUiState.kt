package com.bottari.presentation.view.home.market

import com.bottari.presentation.model.BottariTemplateUiModel

data class TemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
)
