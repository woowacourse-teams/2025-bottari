package com.bottari.presentation.view.home.template

sealed interface TemplateUiEvent {
    sealed interface FetchBottariTemplatesFailure : TemplateUiEvent {
        data object InvalidException : FetchBottariTemplatesFailure

        data object UnexpectedException : FetchBottariTemplatesFailure
    }

    sealed interface SearchBottariTemplatesFailure : TemplateUiEvent {
        data object InvalidException : SearchBottariTemplatesFailure

        data object UnexpectedException : SearchBottariTemplatesFailure
    }
}
