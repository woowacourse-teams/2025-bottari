package com.bottari.core.ui.provider

import androidx.compose.runtime.compositionLocalOf
import com.bottari.core.domain.network.NetworkManager

val LocalNetworkManager = compositionLocalOf<NetworkManager> { error("No NetworkManager provided") }
