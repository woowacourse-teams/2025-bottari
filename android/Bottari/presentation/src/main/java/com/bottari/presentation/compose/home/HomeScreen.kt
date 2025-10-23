package com.bottari.presentation.compose.home

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bottari.presentation.compose.common.navigation.Navigation
import com.bottari.presentation.compose.common.navigation.NavigationController
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.home.bottari.MyBottariScreen
import com.bottari.presentation.compose.home.more.MoreBottariScreen
import com.bottari.presentation.compose.home.template.TemplateBottariScreen
import kotlinx.coroutines.launch


private const val BACK_PRESS_EXIT_TIMEOUT = 2000L

@Composable
fun HomeScreen(
    navigateToPersonalBottariEdit: (Long, Boolean) -> Unit,
    navigateToTeamBottariEdit: (Long, Boolean) -> Unit,
    navigateToPersonalBottariChecklist: (Long, String) -> Unit,
    navigateToTeamBottariChecklist: (Long, String) -> Unit,
    navigateToBrowser: (String) -> Unit,
    navigateToTemplateDetail: (templateId: Long, isMyTemplate: Boolean) -> Unit,
    navigateToTemplateCreate: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalActivity.current

    val snackbarHostState = remember { SnackbarHostState() }
    val navController =
        rememberSaveable(saver = NavigationController.saver) { NavigationController(HomeScreenRoute.Bottari) }
    val currentScreen =
        remember(navController.currentScreen) { navController.currentScreen as HomeScreenRoute }

    BackHandler(enabled = true) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > BACK_PRESS_EXIT_TIMEOUT) {
            backPressedTime = currentTime
            scope.launch {
                snackbarHostState.showSnackbar("한 번 더 누르면 종료됩니다")
            }
            return@BackHandler
        }
        context?.finish()
    }

    Scaffold(
        topBar = { HomeTopAppBar(title = stringResource(currentScreen.labelResId())) },
        bottomBar = {
            HomeBottomNavigationBar(
                screens = HomeScreenRoute.entries,
                selectedTab = currentScreen,
                onTabSelected = { tab ->
                    if (tab == HomeScreenRoute.Template) {
                        navController.navigateOrReset(HomeScreenRoute.Template)
                        return@HomeBottomNavigationBar
                    }
                    navController.navigate(tab)
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = LocalBottariBgColor.current,
    ) { innerPadding ->
        HomeScreenRouter(
            navController = navController,
            navigateToBrowser = navigateToBrowser,
            navigateToTemplateDetail = navigateToTemplateDetail,
            navigateToTemplateCreate = navigateToTemplateCreate,
            navigateToPersonalBottariEdit = navigateToPersonalBottariEdit,
            navigateToTeamBottariEdit = navigateToTeamBottariEdit,
            navigateToPersonalBottariChecklist = navigateToPersonalBottariChecklist,
            navigateToTeamBottariChecklist = navigateToTeamBottariChecklist,
            snackbarState = snackbarHostState,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun HomeScreenRouter(
    navController: NavigationController,
    navigateToBrowser: (String) -> Unit,
    navigateToTemplateDetail: (templateId: Long, isMyTemplate: Boolean) -> Unit,
    navigateToTemplateCreate: () -> Unit,
    navigateToPersonalBottariEdit: (Long, Boolean) -> Unit,
    navigateToTeamBottariEdit: (Long, Boolean) -> Unit,
    navigateToPersonalBottariChecklist: (Long, String) -> Unit,
    navigateToTeamBottariChecklist: (Long, String) -> Unit,
    snackbarState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Navigation(
        navigationController = navController,
        modifier = modifier,
    ) { screen, nav ->
        when (screen) {
            HomeScreenRoute.Template -> {
                TemplateBottariScreen(
                    snackbarState = snackbarState,
                    navigateToTemplateDetail = navigateToTemplateDetail,
                    navigateToTemplateCreate = navigateToTemplateCreate,
                )
            }

            HomeScreenRoute.Bottari ->
                MyBottariScreen(
                    snackbarState = snackbarState,
                    onNavigateToPersonalEdit = navigateToPersonalBottariEdit,
                    onNavigateToTeamEdit = navigateToTeamBottariEdit,
                    onNavigateToPersonalChecklist = navigateToPersonalBottariChecklist,
                    onNavigateToTeamChecklist = navigateToTeamBottariChecklist,
                )

            HomeScreenRoute.More ->
                MoreBottariScreen(
                    snackbarState = snackbarState,
                    onNavigateToBrowser = navigateToBrowser,
                )
        }
    }
}
