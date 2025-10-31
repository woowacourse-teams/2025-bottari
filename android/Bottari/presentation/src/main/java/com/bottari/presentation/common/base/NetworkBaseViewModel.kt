package com.bottari.presentation.common.base

import com.bottari.presentation.util.NetworkManager
import kotlinx.coroutines.flow.StateFlow

abstract class NetworkBaseViewModel<UiState, UiEvent>(
    initialState: UiState,
    networkManager: NetworkManager,
) : FlowBaseViewModel<UiState, UiEvent>(initialState) {
    val isConnected: StateFlow<Boolean> = networkManager.isConnected
}
