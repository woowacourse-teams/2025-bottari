package com.bottari.presentation.view.edit.team.management

sealed interface TeamManagementUiEvent {
    sealed interface FetchTeamMembersFailure : TeamManagementUiEvent {
        data object PermissionException : FetchTeamMembersFailure

        data object NotFoundException : FetchTeamMembersFailure

        data object UnexpectedException : FetchTeamMembersFailure
    }
}
