package com.bottari.presentation.compose.common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier

@Composable
fun Navigation(
    navigationController: NavigationController,
    modifier: Modifier = Modifier,
    screens: @Composable (screen: Screen, nav: NavigationController) -> Unit,
) {
    val stateHolder = rememberSaveableStateHolder()

    Box(modifier = modifier.fillMaxSize()) {
        stateHolder.SaveableStateProvider(navigationController.currentScreen) {
            screens(navigationController.currentScreen, navigationController)
        }
    }
}
