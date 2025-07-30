package com.bottari.presentation.view.home.market

import com.bottari.presentation.model.BottariTemplateUiModel

data class MarketUiState(
    val isLoading: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
)
