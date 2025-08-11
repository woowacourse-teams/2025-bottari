package com.bottari.presentation.view.template.my

import com.bottari.presentation.model.BottariTemplateUiModel

data class MyTemplateUiState(
    val isLoading: Boolean = false,
    val bottariTemplates: List<BottariTemplateUiModel> = emptyList(),
) {
    val isEmpty = bottariTemplates.isEmpty()
}
