package com.bottari.presentation.common.base

import com.bottari.presentation.util.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

abstract class NetworkBaseViewModel<UiState, UiEvent>(
    initialState: UiState,
    private val networkManager: NetworkManager,
) : FlowBaseViewModel<UiState, UiEvent>(initialState) {
    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        observeNetworkState()
    }

    private fun observeNetworkState() {
        launch {
            networkManager.isConnected.collectLatest { isConnected ->
                _isConnected.update { isConnected }
            }
        }
    }
}
