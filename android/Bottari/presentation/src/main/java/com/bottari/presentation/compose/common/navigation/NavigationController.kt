package com.bottari.presentation.compose.common.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue

@Stable
class NavigationController(
    initialScreen: Screen,
) {
    private val backStack = mutableListOf<Screen>()

    var currentScreen by mutableStateOf(initialScreen)
        private set

    internal var onResetRequest: ((Screen) -> Unit)? = null

    fun navigate(screen: Screen) {
        backStack.add(currentScreen)
        currentScreen = screen
    }

    fun navigateOrReset(screen: Screen) {
        if (screen == currentScreen) {
            onResetRequest?.invoke(screen)
            return
        }

        navigate(screen)
    }

    fun popBackStack(): Boolean {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeAt(backStack.lastIndex)
            return true
        }
        return false
    }

    fun clearBackStack() {
        backStack.clear()
    }

    companion object {
        val saver =
            Saver<NavigationController, Pair<Screen, List<Screen>>>(
                save = { controller -> controller.currentScreen to controller.backStack.toList() },
                restore = { (screen, stack) ->
                    val controller = NavigationController(screen)
                    controller.backStack.addAll(stack)
                    controller
                },
            )
    }
}
