package com.bottari.presentation.view.template.detail

import com.bottari.presentation.model.template.BottariTemplateItemUiModel

data class TemplateDetailUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val items: List<BottariTemplateItemUiModel> = emptyList(),
    val author: String = "",
)
