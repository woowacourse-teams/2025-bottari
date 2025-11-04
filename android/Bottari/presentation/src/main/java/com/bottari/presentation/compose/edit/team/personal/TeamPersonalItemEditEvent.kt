package com.bottari.presentation.compose.edit.team.personal

sealed interface TeamPersonalItemEditEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalItemEditEvent

    data object DeleteItemFailureCompose : TeamPersonalItemEditEvent

    data object CreateItemFailureCompose : TeamPersonalItemEditEvent

    data object CreateItemSuccessCompose : TeamPersonalItemEditEvent
}
