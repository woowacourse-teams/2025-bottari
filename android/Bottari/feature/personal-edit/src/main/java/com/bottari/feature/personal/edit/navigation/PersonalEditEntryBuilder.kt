package com.bottari.feature.personal.edit.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.feature.personal.edit.PersonalBottariEditScreen
import com.bottari.feature.personal.edit.navigation.PersonalEditNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.personalEditEntryBuilder() {
    entry<PersonalEditNavKey> { navKey ->
        val navigator = LocalNavigator.current

        PersonalBottariEditScreen(
            bottariId = navKey.bottariId,
            onBackClick = navigator::goBack,
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object PersonalEditEntryBuilderModule {
    @IntoSet
    @Provides
    fun providePersonalEditEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            personalEditEntryBuilder()
        }
}
