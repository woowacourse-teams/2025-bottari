package com.bottari.feature.template.detail

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.template.BottariTemplateItemUiModel

@Immutable
data class TemplateDetailUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val items: List<BottariTemplateItemUiModel> = emptyList(),
    val author: String = "",
)
