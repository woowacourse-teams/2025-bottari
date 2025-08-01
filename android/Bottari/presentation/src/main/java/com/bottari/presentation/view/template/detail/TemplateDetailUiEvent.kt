package com.bottari.presentation.view.template.detail

sealed interface TemplateDetailUiEvent {
    data object FetchBottariDetailFailure : TemplateDetailUiEvent

    data class TakeBottariTemplateSuccess(
        val bottariId: Long?,
    ) : TemplateDetailUiEvent

    data object TakeBottariTemplateFailure : TemplateDetailUiEvent
}
