package com.bottari.presentation.compose.edit.team.personal

sealed interface TeamPersonalEditEditUiEvent {
    data object FetchTeamPersonalItemsFailure : TeamPersonalEditEditUiEvent

    data object DeleteItemFailureCompose : TeamPersonalEditEditUiEvent

    data object CreateItemFailureCompose : TeamPersonalEditEditUiEvent
}
