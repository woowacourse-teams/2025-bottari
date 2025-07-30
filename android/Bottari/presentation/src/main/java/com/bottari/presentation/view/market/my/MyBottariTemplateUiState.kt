package com.bottari.presentation.view.market.my

import com.bottari.presentation.model.BottariTemplateUiModel

data class MyBottariTemplateUiState(
    val isLoading: Boolean = false,
    val bottariTemplates: List<BottariTemplateUiModel> = emptyList(),
)
