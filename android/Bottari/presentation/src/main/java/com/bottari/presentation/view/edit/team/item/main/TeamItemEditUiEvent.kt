package com.bottari.presentation.view.edit.team.item.main

sealed interface TeamItemEditUiEvent {
    data object CreateTeamPersonalItem : TeamItemEditUiEvent

    data object CreateTeamSharedItem : TeamItemEditUiEvent

    data object CreateTeamAssignedItem : TeamItemEditUiEvent
}
