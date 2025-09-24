package com.bottari.presentation.compose.home.template

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Immutable
data class TemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
    val isFetched: Boolean = false,
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
}
