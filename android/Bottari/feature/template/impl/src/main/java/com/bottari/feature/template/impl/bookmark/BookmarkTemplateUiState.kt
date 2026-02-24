package com.bottari.feature.template.impl.bookmark

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.template.BookmarkedTemplateUiModel

@Immutable
data class BookmarkTemplateUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val templates: List<BookmarkedTemplateUiModel> = emptyList(),
) {
    val isEmpty: Boolean = isFetched && templates.isEmpty()
}
