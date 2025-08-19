package com.bottari.presentation.view.checklist.team.main.member

interface TeamMembersStatusUiEvent {
    data object FetchMembersStatusFailure : TeamMembersStatusUiEvent

    data class SendRemindByMemberMessageSuccess(
        val nickname: String,
    ) : TeamMembersStatusUiEvent

    data object SendRemindByMemberMessageFailure : TeamMembersStatusUiEvent
}
