package com.bottari.feature.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.bottari.feature.more.navigation.MoreNavKey
import com.bottari.feature.mybottari.navigation.MyBottariNavKey
import com.bottari.feature.template.navigation.TemplateNavKey

internal data class TopLevelNavItem(
    val icon: ImageVector,
    @param:StringRes val iconTextId: Int,
    @param:StringRes val titleTextId: Int,
)

internal val TEMPLATE =
    TopLevelNavItem(
        icon = Icons.Default.Layers,
        iconTextId = R.string.top_level_nav_item_template_description,
        titleTextId = R.string.top_level_nav_item_template_title_text,
    )

internal val MY_BOTTARI =
    TopLevelNavItem(
        icon = Icons.Default.Home,
        iconTextId = R.string.top_level_nav_item_my_bottari_description,
        titleTextId = R.string.top_level_nav_item_my_bottari_title_text,
    )

internal val MORE =
    TopLevelNavItem(
        icon = Icons.Default.MoreHoriz,
        iconTextId = R.string.top_level_nav_item_more_description,
        titleTextId = R.string.top_level_nav_item_more_title_text,
    )

internal val TOP_LEVEL_NAV_ITEMS =
    mapOf(
        TemplateNavKey to TEMPLATE,
        MyBottariNavKey to MY_BOTTARI,
        MoreNavKey to MORE,
    )
