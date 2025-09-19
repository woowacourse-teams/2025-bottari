package com.bottari.presentation.view.edit.personal.item

sealed interface PersonalItemEditUiEvent {
    data object SaveBottariItemsSuccess : PersonalItemEditUiEvent

    sealed interface SaveBottariItemsFailure : PersonalItemEditUiEvent {
        data object InvalidException : SaveBottariItemsFailure

        data object NotFoundException : SaveBottariItemsFailure

        data object DuplicatedException : SaveBottariItemsFailure

        data object MaximumExceededException : SaveBottariItemsFailure

        data object UnexpectedException : SaveBottariItemsFailure
    }
}
