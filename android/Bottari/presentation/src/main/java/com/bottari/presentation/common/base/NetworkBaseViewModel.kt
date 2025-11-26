package com.bottari.presentation.common.base

import com.bottari.core.domain.network.NetworkManager
import kotlinx.coroutines.flow.StateFlow

abstract class NetworkBaseViewModel<UiState, UiEvent>(
    initialState: UiState,
    networkManager: NetworkManager,
) : FlowBaseViewModel<UiState, UiEvent>(initialState) {
    val isConnected: StateFlow<Boolean> = networkManager.isConnected
}
