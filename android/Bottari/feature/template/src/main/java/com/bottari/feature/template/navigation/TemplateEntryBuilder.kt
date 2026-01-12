package com.bottari.feature.template.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.feature.template.TemplateScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.templateEntryBuilder() {
    entry<MainTabNavKey.TemplateNavKey> {
        TemplateScreen()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TemplateEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideTemplateEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            templateEntryBuilder()
        }
}
