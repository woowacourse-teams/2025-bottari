package com.bottari.presentation.view.home.market

sealed interface TemplateUiEvent {
    data object FetchBottariTemplatesFailure : TemplateUiEvent
}
