package com.bottari.feature.more.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.feature.more.MoreScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.moreEntryBuilder() {
    entry<MainTabNavKey.MoreNavKey> {
        MoreScreen()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object MoreEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideMoreEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            moreEntryBuilder()
        }
}
