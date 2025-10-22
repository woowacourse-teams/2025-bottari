package com.bottari.presentation.compose.common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Navigation(
    navigationController: NavigationController,
    modifier: Modifier = Modifier,
    screens: @Composable (screen: Screen, nav: NavigationController) -> Unit,
) {
    val stateHolder = rememberSaveableStateHolder()
    var resetNonce by remember { mutableIntStateOf(0) }

    DisposableEffect(navigationController) {
        navigationController.onResetRequest = { screen ->
            stateHolder.removeState(screen)
            resetNonce++
        }
        onDispose { navigationController.onResetRequest = null }
    }

    Box(modifier = modifier.fillMaxSize()) {
        key(navigationController.currentScreen, resetNonce) {
            stateHolder.SaveableStateProvider(navigationController.currentScreen) {
                screens(navigationController.currentScreen, navigationController)
            }
        }
    }
}
