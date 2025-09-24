package com.bottari.presentation.compose.home

import androidx.annotation.StringRes
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.navigation.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HomeScreenRoute(
    @StringRes override val labelResId: Int,
    override val route: String,
) : Screen {
    Personal(R.string.home_nav_personal_bottari_title_text, "personal"),
    Team(R.string.home_nav_team_bottari_title_text, "team"),
    Template(R.string.home_nav_template_title_text, "template"),
    More(R.string.home_nav_more_title_text, "more"),
}
