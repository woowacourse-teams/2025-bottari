package com.bottari.presentation.view.template.create

import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.template.SelectableBottariUiModel

data class TemplateCreateUiState(
    val isLoading: Boolean = false,
    val selectedBottariId: Long? = null,
    val bottaries: List<SelectableBottariUiModel> = emptyList(),
) {
    val bottariTitle: String get() = bottaries.find { it.id == selectedBottariId }?.title ?: ""

    val shouldShowEmptyView: Boolean get() = bottaries.isEmpty()

    val currentBottariItems: List<PersonalChecklistItemUiModel>
        get() = bottaries.find { it.id == selectedBottariId }?.items ?: emptyList()

    val canCreateTemplate: Boolean get() = currentBottariItems.isNotEmpty()
}
