package com.bottari.presentation.view.template.create

import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.MyBottariUiModel

data class TemplateCreateUiState(
    val isLoading: Boolean = false,
    val selectedBottariId: Long? = null,
    val bottaries: List<MyBottariUiModel> = emptyList(),
) {
    val selectedBottari: List<BottariItemUiModel>
        get() = bottaries.find { it.id == selectedBottariId }?.items ?: emptyList()
}
