package com.bottari.presentation.compose.home.template

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.template.BottariTemplateHashtagUiModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

@Immutable
data class TemplateUiState(
    private val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val isRefreshingMain: Boolean = false,
    val isRefreshingMy: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
    val myTemplates: List<BottariTemplateUiModel> = emptyList(),
    val searchWord: String = "",
    val chips: List<BottariTemplateHashtagUiModel> = emptyList(),
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
    val showLoading: Boolean = isLoading && isRefreshingMy.not() && isRefreshingMain.not()
}
