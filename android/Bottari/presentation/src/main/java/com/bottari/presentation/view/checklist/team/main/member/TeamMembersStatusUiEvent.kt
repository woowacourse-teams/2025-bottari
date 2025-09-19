package com.bottari.presentation.view.checklist.team.main.member

sealed interface TeamMembersStatusUiEvent {
    data class SendRemindByMemberMessageSuccess(
        val nickname: String,
    ) : TeamMembersStatusUiEvent

    sealed interface FetchMembersStatusFailure : TeamMembersStatusUiEvent {
        data object PermissionException : SendRemindByMemberMessageFailure

        data object NotFoundException : SendRemindByMemberMessageFailure

        data object UnexpectedException : SendRemindByMemberMessageFailure
    }

    sealed interface SendRemindByMemberMessageFailure : TeamMembersStatusUiEvent {
        data object InvalidException : SendRemindByMemberMessageFailure

        data object PermissionException : SendRemindByMemberMessageFailure

        data object NotFoundException : SendRemindByMemberMessageFailure

        data object DuplicatedException : SendRemindByMemberMessageFailure

        data object UnexpectedException : SendRemindByMemberMessageFailure
    }

    sealed interface FetchMemberIdFailure : TeamMembersStatusUiEvent {
        data object NotFoundException : FetchMemberIdFailure

        data object UnexpectedException : FetchMemberIdFailure
    }
}
