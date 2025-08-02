package com.bottari.presentation.view.template.create

sealed interface TemplateCreateUiEvent {
    data object FetchMyBottariesFailure : TemplateCreateUiEvent
}
