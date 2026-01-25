package com.bottari.feature.team.checklist.item

sealed interface ComposeTeamBottariItemStatusUiEvent {
    data object FetchTeamBottariItemStatusFailure : ComposeTeamBottariItemStatusUiEvent

    data object SendRemindSuccess : ComposeTeamBottariItemStatusUiEvent

    data object SendRemindFailure : ComposeTeamBottariItemStatusUiEvent
}
