package com.bottari.core.domain.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkManager {
    val isConnected: StateFlow<Boolean>
}
