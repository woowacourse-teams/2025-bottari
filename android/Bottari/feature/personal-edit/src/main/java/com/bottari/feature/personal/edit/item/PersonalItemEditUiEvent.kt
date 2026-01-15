package com.bottari.feature.personal.edit.item

sealed interface PersonalItemEditUiEvent {
    data object SaveBottariItemFailure : PersonalItemEditUiEvent

    data object FetchBottariItemsFailure : PersonalItemEditUiEvent

    data object DeleteItemFailure : PersonalItemEditUiEvent
}
