package com.bottari.presentation.view.edit.personal.main.rename

sealed interface BottariRenameUiEvent {
    data object SaveBottariTitleSuccess : BottariRenameUiEvent

    data object SaveBottariTitleFailure : BottariRenameUiEvent
}
