package com.bottari.feature.team.edit.assigned

sealed interface TeamAssignedEditUiEvent {
    data object FetchTeamAssignedItemsFailure : TeamAssignedEditUiEvent

    data object DeleteItemFailure : TeamAssignedEditUiEvent

    data object CreateItemFailure : TeamAssignedEditUiEvent

    data object SaveItemFailure : TeamAssignedEditUiEvent
}
