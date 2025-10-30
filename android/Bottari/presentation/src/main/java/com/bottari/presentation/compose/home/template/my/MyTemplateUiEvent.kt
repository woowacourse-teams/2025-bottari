package com.bottari.presentation.compose.home.template.my

sealed interface MyTemplateUiEvent {
    data object FetchBottariTemplatesFailure : MyTemplateUiEvent

    data object DeleteTemplateFailure : MyTemplateUiEvent
}
