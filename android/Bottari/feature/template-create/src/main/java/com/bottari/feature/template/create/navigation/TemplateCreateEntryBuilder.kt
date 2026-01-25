package com.bottari.feature.template.create.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.feature.template.create.CreateTemplateScreen
import com.bottari.feature.template.create.navigation.TemplateCreateNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.templateCreateEntryBuilder() {
    entry<TemplateCreateNavKey> {
        val navigator = LocalNavigator.current

        CreateTemplateScreen(navigateBack = navigator::goBack)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TemplateCreateEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideTemplateCreateEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            templateCreateEntryBuilder()
        }
}
