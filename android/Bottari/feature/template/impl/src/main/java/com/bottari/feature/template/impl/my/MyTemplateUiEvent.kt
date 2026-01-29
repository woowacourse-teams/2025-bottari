package com.bottari.feature.template.impl.my

sealed interface MyTemplateUiEvent {
    data object FetchBottariTemplatesFailure : MyTemplateUiEvent

    data object DeleteTemplateFailure : MyTemplateUiEvent
}
