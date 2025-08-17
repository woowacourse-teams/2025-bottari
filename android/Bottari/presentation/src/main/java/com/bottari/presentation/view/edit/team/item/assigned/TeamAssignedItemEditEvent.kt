package com.bottari.presentation.view.edit.team.item.assigned

sealed interface TeamAssignedItemEditEvent {
    data object FetchTeamAssignedItemsFailure : TeamAssignedItemEditEvent

    data object DeleteItemFailure : TeamAssignedItemEditEvent

    data object AddItemFailure : TeamAssignedItemEditEvent
}
