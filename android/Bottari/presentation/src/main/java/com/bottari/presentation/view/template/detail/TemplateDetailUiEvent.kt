package com.bottari.presentation.view.template.detail

sealed interface TemplateDetailUiEvent {
    sealed interface FetchBottariDetailFailure : TemplateDetailUiEvent {
        data object NotFoundException : FetchBottariDetailFailure

        data object UnexpectedException : FetchBottariDetailFailure
    }

    data class TakeBottariTemplateSuccess(
        val bottariId: Long?,
    ) : TemplateDetailUiEvent

    sealed interface TakeBottariTemplateFailure : TemplateDetailUiEvent {
        data object NotFoundException : TakeBottariTemplateFailure

        data object InvalidException : TakeBottariTemplateFailure

        data object UnexpectedException : TakeBottariTemplateFailure
    }
}
