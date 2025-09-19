package com.bottari.presentation.view.template.create

sealed interface TemplateCreateUiEvent {
    sealed interface FetchMyBottariesFailure : TemplateCreateUiEvent {
        data object NotFoundException : FetchMyBottariesFailure

        data object UnexpectedException : FetchMyBottariesFailure
    }

    sealed interface CreateTemplateFailure : TemplateCreateUiEvent {
        data object NotFoundException : CreateTemplateFailure

        data object InvalidException : CreateTemplateFailure

        data object DuplicatedException : CreateTemplateFailure

        data object UnexpectedException : CreateTemplateFailure
    }

    data object CreateTemplateSuccuss : TemplateCreateUiEvent
}
