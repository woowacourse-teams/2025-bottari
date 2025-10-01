package com.bottari.presentation.compose.home

import com.bottari.presentation.R
import com.bottari.presentation.compose.common.navigation.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HomeScreenRoute(
    override val route: String,
) : Screen {
    Bottari("bottari"),
    Template("template"),
    More("more"),
    ;

    fun labelResId(): Int =
        when (this) {
            Bottari -> R.string.home_nav_my_bottari_title_text
            Template -> R.string.home_nav_template_title_text
            More -> R.string.home_nav_more_title_text
        }
}
