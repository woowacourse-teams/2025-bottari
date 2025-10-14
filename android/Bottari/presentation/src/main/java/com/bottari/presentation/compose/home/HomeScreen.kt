package com.bottari.presentation.compose.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.common.navigation.Navigation
import com.bottari.presentation.compose.common.navigation.NavigationController
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.common.theme.LocalBottariBgColor
import com.bottari.presentation.compose.home.bottari.MyBottariScreen
import com.bottari.presentation.compose.home.more.MoreBottariScreen
import com.bottari.presentation.compose.home.template.TemplateBottariScreen

@Composable
fun HomeScreen(
    navigateToPersonalBottariEdit: (Long, Boolean) -> Unit,
    navigateToTeamBottariEdit: (Long, Boolean) -> Unit,
    navigateToPersonalBottariChecklist: (Long, String) -> Unit,
    navigateToTeamBottariChecklist: (Long, String) -> Unit,
    navigateToBrowser: (String) -> Unit,
    navigateToTemplateDetail: (Long) -> Unit,
    navigateToTemplateCreate: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val navController =
        rememberSaveable(saver = NavigationController.saver) {
            NavigationController(HomeScreenRoute.Bottari)
        }

    val currentScreen =
        remember(navController.currentScreen) {
            navController.currentScreen as HomeScreenRoute
        }

    Scaffold(
        topBar = { HomeTopAppBar(title = stringResource(currentScreen.labelResId())) },
        bottomBar = {
            HomeBottomNavigationBar(
                screens = HomeScreenRoute.entries,
                selectedTab = currentScreen,
                onTabSelected = { tab -> navController.navigate(tab) },
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
    navigateToTemplateDetail: (Long) -> Unit,
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
            HomeScreenRoute.Template ->
                TemplateBottariScreen(
                    navigateToTemplateDetail = navigateToTemplateDetail,
                    navigateToTemplateCreate = navigateToTemplateCreate,
                )

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
                    onNavigateToBrowser = navigateToBrowser,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BottariTheme {
        HomeScreen(
            navigateToPersonalBottariEdit = { _, _ -> },
            navigateToTeamBottariEdit = { _, _ -> },
            navigateToPersonalBottariChecklist = { _, _ -> },
            navigateToTeamBottariChecklist = { _, _ -> },
            navigateToBrowser = {},
            navigateToTemplateDetail = {},
            navigateToTemplateCreate = {},
        )
    }
}
