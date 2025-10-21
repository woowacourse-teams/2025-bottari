package com.bottari.presentation.compose.team.member

interface ComposeTeamMembersStatusUiEvent {
    data object FetchMembersStatusFailure : ComposeTeamMembersStatusUiEvent

    data class SendRemindByMemberMessageSuccess(
        val nickname: String,
    ) : ComposeTeamMembersStatusUiEvent

    data object SendRemindByMemberMessageFailure : ComposeTeamMembersStatusUiEvent

    data object FetchMemberIdFailure : ComposeTeamMembersStatusUiEvent
}
