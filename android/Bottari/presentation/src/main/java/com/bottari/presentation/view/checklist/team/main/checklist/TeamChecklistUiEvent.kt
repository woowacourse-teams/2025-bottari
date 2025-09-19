package com.bottari.presentation.view.checklist.team.main.checklist

sealed interface TeamChecklistUiEvent {
    sealed interface FetchChecklistFailure : TeamChecklistUiEvent {
        data object PermissionException : FetchChecklistFailure

        data object NotFoundException : FetchChecklistFailure

        data object UnexpectedException : FetchChecklistFailure
    }

    sealed interface CheckItemFailure : TeamChecklistUiEvent {
        data object NotFoundException : CheckItemFailure

        data object DuplicatedException : CheckItemFailure

        data object UnexpectedException : CheckItemFailure
    }
}
