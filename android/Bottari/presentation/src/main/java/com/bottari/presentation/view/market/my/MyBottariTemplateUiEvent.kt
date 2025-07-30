package com.bottari.presentation.view.market.my

sealed interface MyBottariTemplateUiEvent {
    data object FetchMyTemplateFailure : MyBottariTemplateUiEvent

    data object DeleteMyTemplateSuccess : MyBottariTemplateUiEvent

    data object DeleteMyTemplateFailure : MyBottariTemplateUiEvent
}
