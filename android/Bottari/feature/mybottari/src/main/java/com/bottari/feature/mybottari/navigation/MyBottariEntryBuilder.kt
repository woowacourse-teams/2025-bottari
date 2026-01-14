package com.bottari.feature.mybottari.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.feature.mybottari.MyBottariScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.myBottariEntryBuilder() {
    entry<MainTabNavKey.MyBottariNavKey> {
        val navigator = LocalNavigator.current
        MyBottariScreen()
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
