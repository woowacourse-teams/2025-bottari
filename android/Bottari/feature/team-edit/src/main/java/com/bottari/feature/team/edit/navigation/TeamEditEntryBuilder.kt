package com.bottari.feature.team.edit.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.feature.team.edit.main.TeamBottariEditScreen
import com.bottari.feature.team.edit.navigation.TeamEditNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.teamEditEntryBuilder() {
    entry<TeamEditNavKey> { navKey ->
        TeamBottariEditScreen(
            bottariId = navKey.bottariId,
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TeamEditEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideTeamEditEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            teamEditEntryBuilder()
        }
}
