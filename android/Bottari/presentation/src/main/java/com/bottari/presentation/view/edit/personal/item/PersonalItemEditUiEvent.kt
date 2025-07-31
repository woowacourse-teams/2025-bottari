package com.bottari.presentation.view.edit.personal.item

sealed interface PersonalItemEditUiEvent {
    data object SaveBottariItemsSuccess : PersonalItemEditUiEvent

    data object SaveBottariItemsFailure : PersonalItemEditUiEvent
}
