package com.bottari.presentation.compose.home.template

sealed interface TemplateUiEvent {
    data object FetchBottariTemplatesFailure : TemplateUiEvent
}
