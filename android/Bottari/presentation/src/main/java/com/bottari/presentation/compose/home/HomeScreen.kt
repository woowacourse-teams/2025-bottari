package com.bottari.presentation.compose.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToPersonalBottariEdit: () -> Unit = {}, // Todo: 다른 Activity로 가는 로직
) {
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(currentScreen.labelResId()),
                        style = BottariTheme.typography.bold20.toTextStyle(),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = BottariTheme.colors.gray50,
                        titleContentColor = BottariTheme.colors.black,
                    ),
            )
        },
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
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun HomeScreenRouter(
    navController: NavigationController,
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
                    onNavigateToPersonal = { nav.navigate(HomeScreenRoute.Personal) },
                    onNavigateToTeam = { nav.navigate(HomeScreenRoute.Team) },
                    onNavigateToMore = { nav.navigate(HomeScreenRoute.More) },
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
        HomeScreen()
    }
}
