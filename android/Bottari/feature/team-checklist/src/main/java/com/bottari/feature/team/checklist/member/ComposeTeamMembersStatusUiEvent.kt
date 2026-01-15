package com.bottari.feature.team.checklist.member

sealed interface ComposeTeamMembersStatusUiEvent {
    data object FetchMembersStatusFailure : ComposeTeamMembersStatusUiEvent

    data class SendRemindByMemberMessageSuccess(
        val nickname: String,
    ) : ComposeTeamMembersStatusUiEvent

    data object SendRemindByMemberMessageFailure : ComposeTeamMembersStatusUiEvent
}
