package com.bottari.presentation.compose.edit.team.assigned

sealed interface TeamAssignedItemEditUiEvent {
    data object FetchTeamAssignedItemsFailureUi : TeamAssignedItemEditUiEvent

    data object DeleteItemFailureUi : TeamAssignedItemEditUiEvent

    data object CreateItemFailureUi : TeamAssignedItemEditUiEvent

    data object CreateItemSuccessUi : TeamAssignedItemEditUiEvent

    data object SaveItemSuccessUi : TeamAssignedItemEditUiEvent

    data object SaveItemFailureUi : TeamAssignedItemEditUiEvent
}
