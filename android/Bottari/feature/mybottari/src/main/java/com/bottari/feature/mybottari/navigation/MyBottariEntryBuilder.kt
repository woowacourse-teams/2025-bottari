package com.bottari.feature.mybottari.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.mybottari.MyBottariScreen
import com.bottari.feature.personal.checklist.navigation.PersonalChecklistNavKey
import com.bottari.feature.personal.edit.api.PersonalEditNavKey
import com.bottari.feature.team.checklist.navigation.TeamChecklistNavKey
import com.bottari.feature.team.edit.navigation.TeamEditNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.myBottariEntryBuilder() {
    entry<MyBottariNavKey> {
        val navigator = LocalNavigator.current

        MyBottariScreen(
            snackbarState = LocalSnackbarHostState.current,
            onNavigateToPersonalEdit = { bottariId, isNew ->
                navigator.navigate(PersonalEditNavKey(bottariId, isNew))
            },
            onNavigateToTeamEdit = { bottariId, isNew ->
                navigator.navigate(TeamEditNavKey(bottariId, isNew))
            },
            onNavigateToPersonalChecklist = { bottariId, title ->
                navigator.navigate(PersonalChecklistNavKey(bottariId, title))
            },
            onNavigateToTeamChecklist = { bottariId, title ->
                navigator.navigate(TeamChecklistNavKey(bottariId, title))
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object MyBottariEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideMyBottariEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            myBottariEntryBuilder()
        }
}
