package com.bottari.presentation.compose.edit.team.personal

sealed interface TeamPersonalItemEditUiEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalItemEditUiEvent

    data object DeleteItemFailureCompose : TeamPersonalItemEditUiEvent

    data object CreateItemFailureCompose : TeamPersonalItemEditUiEvent

    data object CreateItemSuccessCompose : TeamPersonalItemEditUiEvent
}
