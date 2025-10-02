package com.bottari.presentation.view.edit.personal.item

sealed interface PersonalItemEditUiEvent {
    data object SaveBottariItemFailure : PersonalItemEditUiEvent

    data object FetchBottariItemsFailure : PersonalItemEditUiEvent

    data object DeleteItemFailure : PersonalItemEditUiEvent
}
