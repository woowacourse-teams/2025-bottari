package com.bottari.feature.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.source.rememberNoRippleInteractionSource

@Composable
fun MainBottomNavigation(
    selectedTab: NavKey,
    onTabSelected: (NavKey) -> Unit,
) {
    NavigationBar(
        containerColor = BottariTheme.colors.white,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
    ) {
        TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
            NavigationBarItem(
                selected = navKey == selectedTab,
                onClick = { onTabSelected(navKey) },
                icon = { MainBottomNavigationIcon(navItem = navItem) },
                label = { MainBottomNavigationLabel(navItem.titleTextId) },
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = BottariTheme.colors.transparent,
                        selectedIconColor = BottariTheme.colors.black,
                        selectedTextColor = BottariTheme.colors.black,
                        unselectedIconColor = BottariTheme.colors.gray500,
                        unselectedTextColor = BottariTheme.colors.gray500,
                    ),
                interactionSource = rememberNoRippleInteractionSource(),
            )
        }
    }
}

@Composable
private fun MainBottomNavigationIcon(navItem: TopLevelNavItem) {
    Icon(
        imageVector = navItem.icon,
        contentDescription = stringResource(navItem.iconTextId),
        modifier = Modifier.size(32.dp),
    )
}

@Composable
private fun MainBottomNavigationLabel(
    @StringRes textId: Int,
) {
    Text(
        text = stringResource(textId),
        style = BottariTheme.typography.medium14.toTextStyle(),
    )
}

@Preview
@Composable
private fun MainBottomNavigationPreview() {
    BottariTheme {
        MainBottomNavigation(
            selectedTab = TOP_LEVEL_NAV_ITEMS.keys.first(),
            onTabSelected = {},
        )
    }
}
