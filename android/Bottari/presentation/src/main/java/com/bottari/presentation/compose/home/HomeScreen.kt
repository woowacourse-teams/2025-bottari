package com.bottari.presentation.compose.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.bottari.presentation.compose.home.more.MoreBottariScreen
import com.bottari.presentation.compose.home.personal.PersonalBottariScreen
import com.bottari.presentation.compose.home.team.TeamBottariScreen
import com.bottari.presentation.compose.home.template.TemplateBottariScreen

@Composable
fun HomeScreen(navigateToTemplateDetail: (Long) -> Unit) {
    val navController =
        rememberSaveable(saver = NavigationController.saver) {
            NavigationController(HomeScreenRoute.Personal)
        }

    val currentScreen =
        remember(navController.currentScreen) {
            navController.currentScreen as HomeScreenRoute
        }

    Scaffold(
        containerColor = LocalBottariBgColor.current,
        topBar = { HomeTopAppBar(title = stringResource(currentScreen.labelResId())) },
        bottomBar = {
            HomeBottomNavigationBar(
                screens = HomeScreenRoute.entries,
                selectedTab = currentScreen,
                onTabSelected = { tab -> navController.navigate(tab) },
            )
        },
    ) { innerPadding ->
        HomeScreenRouter(
            navController = navController,
            navigateToTemplateDetail = navigateToTemplateDetail,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun HomeScreenRouter(
    navController: NavigationController,
    navigateToTemplateDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Navigation(
        navigationController = navController,
        modifier = modifier,
    ) { screen, nav ->
        when (screen) {
            HomeScreenRoute.Personal ->
                PersonalBottariScreen(
                    onNavigateToTeam = { nav.navigate(HomeScreenRoute.Team) },
                    onNavigateToTemplate = { nav.navigate(HomeScreenRoute.Template) },
                    onNavigateToMore = { nav.navigate(HomeScreenRoute.More) },
                )

            HomeScreenRoute.Team ->
                TeamBottariScreen(
                    onNavigateToPersonal = { nav.navigate(HomeScreenRoute.Personal) },
                    onNavigateToTemplate = { nav.navigate(HomeScreenRoute.Template) },
                    onNavigateToMore = { nav.navigate(HomeScreenRoute.More) },
                )

            HomeScreenRoute.Template ->
                TemplateBottariScreen(
                    navigateToTemplateDetail = navigateToTemplateDetail,
                )

            HomeScreenRoute.More ->
                MoreBottariScreen(
                    onNavigateToPersonal = { nav.navigate(HomeScreenRoute.Personal) },
                    onNavigateToTeam = { nav.navigate(HomeScreenRoute.Team) },
                    onNavigateToTemplate = { nav.navigate(HomeScreenRoute.Template) },
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BottariTheme {
        HomeScreen(
            navigateToTemplateDetail = {},
        )
    }
}
