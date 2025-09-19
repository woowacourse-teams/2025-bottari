package com.bottari.presentation.view.checklist.team.main.status

sealed interface TeamBottariStatusUiEvent {
    data object SendRemindSuccess : TeamBottariStatusUiEvent

    sealed interface SendRemindFailure : TeamBottariStatusUiEvent {
        data object InvalidException : SendRemindFailure

        data object NotFoundException : SendRemindFailure

        data object PermissionException : SendRemindFailure

        data object UnexpectedException : SendRemindFailure
    }

    sealed interface FetchTeamBottariStatusFailure : TeamBottariStatusUiEvent {
        data object PermissionException : FetchTeamBottariStatusFailure

        data object UnexpectedException : FetchTeamBottariStatusFailure
    }
}
