package com.bottari.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
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
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.core.navigation.Navigator
import com.bottari.core.navigation.rememberNavigationState
import com.bottari.core.navigation.toEntries
import com.bottari.core.ui.provider.LocalSnackbarHostState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BottariTheme {
                App(entryBuilders = entryBuilders)
            }
        }
    }
}

@Composable
fun App(entryBuilders: Set<EntryProviderScope<NavKey>.() -> Unit>) {
    val navigationState =
        rememberNavigationState(MainTabNavKey.MyBottariNavKey, TOP_LEVEL_NAV_ITEMS.keys)
    val navigator = remember { Navigator(navigationState) }
    val snackbarState = remember { SnackbarHostState() }
    val entryProvider =
        entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalSnackbarHostState provides snackbarState,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarState) },
            bottomBar = {
                MainBottomNavigation(
                    selectedTab = navigationState.currentKey,
                    onTabSelected = navigator::navigate,
                )
            },
            containerColor = BottariTheme.colors.white,
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavDisplay(
                    entries = navigationState.toEntries(entryProvider),
                    onBack = navigator::goBack,
                )
            }
        }
    }
}
