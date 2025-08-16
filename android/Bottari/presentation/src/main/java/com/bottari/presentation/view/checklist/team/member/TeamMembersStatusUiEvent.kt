package com.bottari.presentation.view.checklist.team.member

interface TeamMembersStatusUiEvent {
    data object FetchMembersStatusFailure : TeamMembersStatusUiEvent

    data object SendRemindByMemberMessageFailure : TeamMembersStatusUiEvent
}
