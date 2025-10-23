package com.bottari.presentation.compose.home.template

sealed interface TemplateUiEvent {
    data object SearchTemplateSuccess : TemplateUiEvent

    data object FetchBottariTemplatesFailure : TemplateUiEvent

    data object MainTemplatesRefreshFinished : TemplateUiEvent

    data object MyTemplatesRefreshFinished : TemplateUiEvent

    data object DeleteBottariTemplateSuccess : TemplateUiEvent

    data object DeleteBottariTemplateFailure : TemplateUiEvent

    data object AddBookmarkFailure : TemplateUiEvent

    data object DeleteBookmarkFailure : TemplateUiEvent
}
