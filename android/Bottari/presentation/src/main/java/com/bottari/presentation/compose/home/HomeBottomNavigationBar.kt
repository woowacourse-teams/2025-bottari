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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.core.designsystem.theme.BottariTheme
import com.bottari.core.ui.source.NoRippleInteractionSource
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.navigation.Screen

@Composable
fun HomeBottomNavigationBar(
    screens: List<HomeScreenRoute>,
    selectedTab: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = BottariTheme.colors.black
    val unselectedColor = BottariTheme.colors.gray500
    val noRippleInteractionSource = remember { NoRippleInteractionSource() }

    NavigationBar(
        containerColor = BottariTheme.colors.white,
        modifier = modifier.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
    ) {
        key(selectedTab) {
            screens.forEach { screen ->
                NavigationBarItem(
                    selected = screen == selectedTab,
                    onClick = { onTabSelected(screen) },
                    icon = { HomeNavigationBarIcon(screen) },
                    alwaysShowLabel = false,
                    colors =
                        NavigationBarItemDefaults.colors(
                            indicatorColor = BottariTheme.colors.transparent,
                            selectedIconColor = selectedColor,
                            selectedTextColor = selectedColor,
                            unselectedIconColor = unselectedColor,
                            unselectedTextColor = unselectedColor,
                        ),
                    interactionSource = noRippleInteractionSource,
                )
            }
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
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(screen.labelResId()),
            modifier = modifier.size(32.dp),
        )

        Text(
            text = stringResource(screen.labelResId()),
            style = BottariTheme.typography.medium14.toTextStyle(),
        )
    }
}

private fun HomeScreenRoute.toIconResId(): Int =
    when (this) {
        HomeScreenRoute.Template -> R.drawable.ic_template
        HomeScreenRoute.Bottari -> R.drawable.ic_home
        HomeScreenRoute.More -> R.drawable.ic_more_horizontal
    }

@Preview(showBackground = true)
@Composable
private fun HomeBottomNavigationBarPreview() {
    BottariTheme {
        HomeBottomNavigationBar(
            screens = HomeScreenRoute.entries.toList(),
            selectedTab = HomeScreenRoute.Bottari,
            onTabSelected = {},
        )
    }
}
