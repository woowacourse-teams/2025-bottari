package com.bottari.presentation.compose.edit.personal.item

sealed interface PersonalItemEditUiEvent {
    data object SaveBottariItemFailure : PersonalItemEditUiEvent

    data object FetchBottariItemsFailure : PersonalItemEditUiEvent

    data object DeleteItemFailure : PersonalItemEditUiEvent
}
