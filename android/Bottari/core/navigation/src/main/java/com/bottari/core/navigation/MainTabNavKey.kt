package com.bottari.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainTabNavKey : NavKey {
    @Serializable
    data object MyBottariNavKey : MainTabNavKey

    @Serializable
    data object TemplateNavKey : MainTabNavKey

    @Serializable
    data object MoreNavKey : MainTabNavKey
}
