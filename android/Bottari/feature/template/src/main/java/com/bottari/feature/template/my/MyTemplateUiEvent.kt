package com.bottari.feature.template.my

sealed interface MyTemplateUiEvent {
    data object FetchBottariTemplatesFailure : MyTemplateUiEvent

    data object DeleteTemplateFailure : MyTemplateUiEvent
}
