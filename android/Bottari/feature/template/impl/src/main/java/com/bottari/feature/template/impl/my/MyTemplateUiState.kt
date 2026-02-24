package com.bottari.feature.template.impl.my

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.template.BottariTemplateUiModel

@Immutable
data class MyTemplateUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val isRefreshing: Boolean = false,
    val templates: List<BottariTemplateUiModel> = emptyList(),
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
    val showLoading: Boolean = isLoading && isRefreshing.not()
}
