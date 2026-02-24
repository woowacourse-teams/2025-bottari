package com.bottari.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bottari.bottari.designsystem.theme.LocalBottariBgColor
import com.bottari.core.domain.network.NetworkManager
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.Navigator
import com.bottari.core.navigation.rememberNavigationState
import com.bottari.core.navigation.toEntries
import com.bottari.core.ui.provider.LocalNetworkManager
import com.bottari.core.ui.provider.LocalSnackbarHostState

@Composable
fun App(
    entryBuilders: Set<EntryProviderScope<NavKey>.() -> Unit>,
    startKey: NavKey,
    networkManager: NetworkManager,
) {
    val navigationState = rememberNavigationState(startKey, TOP_LEVEL_NAV_ITEMS.keys)
    val navigator = remember { Navigator(navigationState) }
    val snackbarState = remember { SnackbarHostState() }

    val entryProvider =
        entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalSnackbarHostState provides snackbarState,
        LocalNetworkManager provides networkManager,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarState) },
            bottomBar = {
                if (navigationState.currentKey in TOP_LEVEL_NAV_ITEMS.keys) {
                    MainBottomNavigation(
                        selectedTab = navigationState.currentKey,
                        onTabSelected = navigator::navigate,
                    )
                }
            },
            containerColor = LocalBottariBgColor.current,
        ) { innerPadding ->
            NavDisplay(
                entries = navigationState.toEntries(entryProvider),
                onBack = navigator::goBack,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
