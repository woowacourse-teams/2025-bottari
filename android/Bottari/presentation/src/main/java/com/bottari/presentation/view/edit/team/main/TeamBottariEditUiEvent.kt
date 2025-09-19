package com.bottari.presentation.view.edit.team.main

sealed interface TeamBottariEditUiEvent {
    sealed interface FetchTeamBottariDetailFailure : TeamBottariEditUiEvent {
        data object PermissionException : FetchTeamBottariDetailFailure

        data object NotFoundException : FetchTeamBottariDetailFailure

        data object UnexpectedException : FetchTeamBottariDetailFailure
    }
}
