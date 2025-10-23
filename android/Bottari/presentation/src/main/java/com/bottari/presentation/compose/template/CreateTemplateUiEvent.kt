package com.bottari.presentation.compose.template

sealed interface CreateTemplateUiEvent {
    data object FetchMyBottariesFailure : CreateTemplateUiEvent

    data object CreateTemplateSuccess : CreateTemplateUiEvent

    data object CreateTemplateFailure : CreateTemplateUiEvent
}
