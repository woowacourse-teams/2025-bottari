package com.bottari.presentation.compose.home.template

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Immutable
data class TemplateUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
    val searchWord: String = "",
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
}
