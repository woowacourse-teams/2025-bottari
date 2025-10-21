package com.bottari.presentation.compose.team

import com.bottari.presentation.R
import com.bottari.presentation.compose.common.navigation.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TeamChecklistScreenRoute(
    override val route: String,
) : Screen {
    Checklist("checklist"),
    Item("item"),
    Person("person"),
    ;

    fun labelResId(): Int =
        when (this) {
            Checklist -> R.string.home_nav_template_title_text
            Item -> R.string.home_nav_my_bottari_title_text
            Person -> R.string.home_nav_more_title_text
        }
}
