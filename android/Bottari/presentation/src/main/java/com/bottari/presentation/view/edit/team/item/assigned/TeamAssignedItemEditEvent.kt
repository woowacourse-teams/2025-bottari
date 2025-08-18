package com.bottari.presentation.view.edit.team.item.assigned

sealed interface TeamAssignedItemEditEvent {
    data object FetchTeamAssignedItemsFailure : TeamAssignedItemEditEvent

    data object DeleteItemFailure : TeamAssignedItemEditEvent

    data object CreateItemFailure : TeamAssignedItemEditEvent

    data object CreateItemSuccess : TeamAssignedItemEditEvent
}
