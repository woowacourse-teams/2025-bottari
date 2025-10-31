package com.bottari.presentation.compose.edit.team.assigned

sealed interface ComposeTeamAssignedItemEditUiEvent {
    data object FetchTeamAssignedItemsFailureUi : ComposeTeamAssignedItemEditUiEvent

    data object DeleteItemFailureComposeUi : ComposeTeamAssignedItemEditUiEvent

    data object CreateItemFailureComposeUi : ComposeTeamAssignedItemEditUiEvent

    data object CreateItemSuccessComposeUi : ComposeTeamAssignedItemEditUiEvent

    data object SaveItemSuccessUi : ComposeTeamAssignedItemEditUiEvent

    data object SaveItemFailureUi : ComposeTeamAssignedItemEditUiEvent
}
