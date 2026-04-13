package com.bottari.feature.personal.edit.impl.rename

sealed interface BottariRenameUiEvent {
    data object SaveBottariTitleSuccess : BottariRenameUiEvent

    data object SaveBottariTitleFailure : BottariRenameUiEvent
}
