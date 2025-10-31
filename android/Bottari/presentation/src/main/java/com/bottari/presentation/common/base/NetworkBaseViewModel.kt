package com.bottari.presentation.common.base

import com.bottari.presentation.util.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

abstract class NetworkBaseViewModel<UiState, UiEvent>(
    initialState: UiState,
    networkManager: NetworkManager,
) : FlowBaseViewModel<UiState, UiEvent>(initialState) {
    val isConnected: StateFlow<Boolean> = networkManager.isConnected
}
