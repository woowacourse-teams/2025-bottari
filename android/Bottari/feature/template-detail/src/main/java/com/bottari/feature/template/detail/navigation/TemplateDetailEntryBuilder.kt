package com.bottari.feature.template.detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.personal.edit.api.PersonalEditNavKey
import com.bottari.feature.template.detail.TemplateDetailScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.templateDetailEntryBuilder() {
    entry<TemplateDetailNavKey> { navKey ->
        val navigator = LocalNavigator.current

        TemplateDetailScreen(
            snackbarState = LocalSnackbarHostState.current,
            templateId = navKey.templateId,
            isMyTemplate = navKey.isMyTemplate,
            isBookmark = navKey.isBookmark,
            navigateToBack = navigator::goBack,
            navigateToPersonalEdit = { bottariId ->
                val navKey = PersonalEditNavKey(bottariId = bottariId, isNew = true)
                navigator.navigate(navKey)
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TemplateDetailEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideTemplateDetailEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            templateDetailEntryBuilder()
        }
}
