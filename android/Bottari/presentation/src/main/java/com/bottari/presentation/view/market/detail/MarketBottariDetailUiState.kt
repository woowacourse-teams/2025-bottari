package com.bottari.presentation.view.market.detail

import com.bottari.presentation.model.BottariTemplateItemUiModel

data class MarketBottariDetailUiState(
    val isLoading: Boolean = false,
    val templateId: Long,
    val title: String = "",
    val items: List<BottariTemplateItemUiModel> = emptyList(),
    val author: String = "",
)
