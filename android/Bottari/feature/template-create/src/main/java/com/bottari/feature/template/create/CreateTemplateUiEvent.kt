package com.bottari.feature.template.create

sealed interface CreateTemplateUiEvent {
    data object FetchMyBottariesFailure : CreateTemplateUiEvent

    data object CreateTemplateSuccess : CreateTemplateUiEvent

    data object CreateTemplateFailure : CreateTemplateUiEvent
}
