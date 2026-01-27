package com.bottari.presentation.compose.edit.team.personal

sealed interface TeamPersonalEditUiEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalEditUiEvent

    data object DeleteItemFailureCompose : TeamPersonalEditUiEvent

    data object CreateItemFailureCompose : TeamPersonalEditUiEvent
}
