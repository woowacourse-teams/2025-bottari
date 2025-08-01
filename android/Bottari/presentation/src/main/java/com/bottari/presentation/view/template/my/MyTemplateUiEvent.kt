package com.bottari.presentation.view.template.my

sealed interface MyTemplateUiEvent {
    data object FetchMyTemplateFailure : MyTemplateUiEvent

    data object DeleteMyTemplateSuccess : MyTemplateUiEvent

    data object DeleteMyTemplateFailure : MyTemplateUiEvent
}
