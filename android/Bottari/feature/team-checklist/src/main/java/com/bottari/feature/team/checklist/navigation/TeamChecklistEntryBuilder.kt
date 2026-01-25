package com.bottari.feature.team.checklist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.team.checklist.TeamBottariScreen
import com.bottari.feature.team.checklist.navigation.TeamChecklistNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.teamChecklistEntryBuilder() {
    entry<TeamChecklistNavKey> { navKey ->
        val navigator = LocalNavigator.current

        TeamBottariScreen(
            snackbarState = LocalSnackbarHostState.current,
            bottariId = navKey.bottariId,
            bottariTitle = navKey.bottariTitle,
            notificationFlag = navKey.notificationFlag,
            navigateBack = navigator::goBack,
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TeamChecklistEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideTeamChecklistEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            teamChecklistEntryBuilder()
        }
}
