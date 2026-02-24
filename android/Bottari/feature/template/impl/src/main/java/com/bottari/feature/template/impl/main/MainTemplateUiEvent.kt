package com.bottari.feature.template.impl.main

sealed interface MainTemplateUiEvent {
    data object SearchTemplateSuccess : MainTemplateUiEvent

    data object FetchBottariTemplatesFailure : MainTemplateUiEvent

    data object AddBookmarkFailure : MainTemplateUiEvent

    data object DeleteBookmarkFailure : MainTemplateUiEvent
}
