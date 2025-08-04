package com.bottari.presentation.view.home.template

sealed interface TemplateUiEvent {
    data object FetchBottariTemplatesFailure : TemplateUiEvent
}
