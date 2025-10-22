package com.bottari.presentation.compose.template

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.bottari.personal.BottariDetailUiModel

@Immutable
data class CreateTemplateUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val myTemplates: List<BottariDetailUiModel> = emptyList(),
    val selectedBottariTitle: String = "",
    val selectedBottariItems: List<String> = emptyList(),
    val description: String = "",
    val writingHashtag: String = "",
    val canAddHashtag: Boolean = false,
    val hashtags: List<String> = emptyList(),
) {
    val isSelected: Boolean = selectedBottariTitle.isNotBlank() && selectedBottariItems.isNotEmpty()
    val canCreate: Boolean = isSelected && hashtags.size >= 2
}
