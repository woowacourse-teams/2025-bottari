package com.bottari.presentation.compose.edit.personal.rename

sealed interface BottariRenameUiEvent {
    data object SaveBottariTitleSuccess : BottariRenameUiEvent

    data object SaveBottariTitleFailure : BottariRenameUiEvent
}
