package com.bottari.presentation.view.template.my

sealed interface MyTemplateUiEvent {
    sealed interface FetchMyTemplateFailure : MyTemplateUiEvent {
        data object NotFoundException : FetchMyTemplateFailure

        data object UnexpectedException : FetchMyTemplateFailure
    }

    sealed interface DeleteMyTemplateFailure : MyTemplateUiEvent {
        data object PermissionException : DeleteMyTemplateFailure

        data object NotFoundException : DeleteMyTemplateFailure

        data object UnexpectedException : DeleteMyTemplateFailure
    }

    data object DeleteMyTemplateSuccess : MyTemplateUiEvent
}
