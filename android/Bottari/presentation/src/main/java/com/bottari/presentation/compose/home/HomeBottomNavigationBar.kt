package com.bottari.presentation.compose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.navigation.Screen
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun HomeBottomNavigationBar(
    screens: List<HomeScreenRoute>,
    selectedTab: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = BottariTheme.colors.black
    val unselectedColor = BottariTheme.colors.gray500

    NavigationBar(
        containerColor = BottariTheme.colors.white,
        modifier = modifier.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { HomeNavigationBarIcon(screen) },
                alwaysShowLabel = false,
                selected = screen == selectedTab,
                onClick = { onTabSelected(screen) },
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = BottariTheme.colors.transparent,
                        selectedIconColor = selectedColor,
                        selectedTextColor = selectedColor,
                        unselectedIconColor = unselectedColor,
                        unselectedTextColor = unselectedColor,
                    ),
            )
        }
    }
}

@Composable
private fun HomeNavigationBarIcon(
    screen: HomeScreenRoute,
    modifier: Modifier = Modifier,
) {
    val iconRes = remember(screen) { screen.toIconResId() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(screen.labelResId),
            modifier = modifier.size(32.dp),
        )

        Text(
            text = stringResource(screen.labelResId),
            style = BottariTheme.typography.medium12.toTextStyle(),
        )
    }
}

private fun HomeScreenRoute.toIconResId(): Int =
    when (this) {
        HomeScreenRoute.Personal -> R.drawable.ic_personal
        HomeScreenRoute.Team -> R.drawable.ic_team
        HomeScreenRoute.Template -> R.drawable.ic_template
        HomeScreenRoute.More -> R.drawable.ic_more_horizontal
    }

@Preview(showBackground = true)
@Composable
private fun HomeBottomNavigationBarPreview() {
    BottariTheme {
        HomeBottomNavigationBar(
            screens = HomeScreenRoute.entries.toList(),
            selectedTab = HomeScreenRoute.Personal,
            onTabSelected = {},
        )
    }
}
