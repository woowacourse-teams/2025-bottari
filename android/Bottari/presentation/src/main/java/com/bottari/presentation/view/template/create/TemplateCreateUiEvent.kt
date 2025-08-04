package com.bottari.presentation.view.template.create

sealed interface TemplateCreateUiEvent {
    data object FetchMyBottariesFailure : TemplateCreateUiEvent

    data object CreateTemplateFailure : TemplateCreateUiEvent

    data object CreateTemplateSuccuss : TemplateCreateUiEvent
}
