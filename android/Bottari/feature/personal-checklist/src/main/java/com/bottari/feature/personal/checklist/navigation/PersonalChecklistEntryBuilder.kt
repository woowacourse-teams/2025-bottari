package com.bottari.feature.personal.checklist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.personal.checklist.PersonalBottariScreen
import com.bottari.feature.personal.checklist.navigation.PersonalChecklistNavKey
import com.bottari.feature.personal.edit.navigation.PersonalEditNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.personalChecklistEntryBuilder() {
    entry<PersonalChecklistNavKey> { navKey ->
        val navigator = LocalNavigator.current

        PersonalBottariScreen(
            snackbarState = LocalSnackbarHostState.current,
            bottariId = navKey.bottariId,
            bottariTitle = navKey.bottariTitle,
            notificationFlag = navKey.notificationFlag,
            navigateToBack = navigator::goBack,
            navigateToEdit = {
                navigator.navigate(
                    PersonalEditNavKey(bottariId = navKey.bottariId, isNew = false),
                )
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object PersonalChecklistEntryBuilderModule {
    @IntoSet
    @Provides
    fun providePersonalChecklistEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            personalChecklistEntryBuilder()
        }
}
