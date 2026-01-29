package com.bottari.feature.template.impl.main

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.template.BottariTemplateHashtagUiModel
import com.bottari.core.ui.model.template.BottariTemplateUiModel

@Immutable
data class MainTemplateUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val isRefreshing: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
    val popularHashtags: List<BottariTemplateHashtagUiModel> = emptyList(),
    val chip: BottariTemplateHashtagUiModel? = null,
    val searchWord: String = "",
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
    val showLoading: Boolean = isLoading && isRefreshing.not()
}
