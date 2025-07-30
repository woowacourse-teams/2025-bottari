package com.bottari.presentation.view.home.market

sealed interface MarketUiEvent {
    data object FetchBottariTemplatesFailure : MarketUiEvent
}
