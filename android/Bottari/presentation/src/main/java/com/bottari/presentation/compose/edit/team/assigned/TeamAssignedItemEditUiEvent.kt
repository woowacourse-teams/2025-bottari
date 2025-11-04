package com.bottari.presentation.compose.edit.team.assigned

sealed interface TeamAssignedItemEditUiEvent {
    data object FetchTeamAssignedItemsFailure : TeamAssignedItemEditUiEvent

    data object DeleteItemFailure : TeamAssignedItemEditUiEvent

    data object CreateItemFailure : TeamAssignedItemEditUiEvent

    data object CreateItemSuccess : TeamAssignedItemEditUiEvent

    data object SaveItemSuccess : TeamAssignedItemEditUiEvent

    data object SaveItemFailure : TeamAssignedItemEditUiEvent
}
