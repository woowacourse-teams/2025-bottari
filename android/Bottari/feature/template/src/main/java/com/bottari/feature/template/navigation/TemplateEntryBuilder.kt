package com.bottari.feature.template.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.template.TemplateScreen
import com.bottari.feature.template.create.navigation.TemplateCreateNavKey
import com.bottari.feature.template.detail.navigation.TemplateDetailNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.templateEntryBuilder() {
    entry<TemplateNavKey> {
        val navigator = LocalNavigator.current

        TemplateScreen(
            snackbarState = LocalSnackbarHostState.current,
            navigateToTemplateDetail = { id, isMyTemplate, isBookmark ->
                TemplateDetailNavKey(
                    templateId = id,
                    isMyTemplate = isMyTemplate,
                    isBookmark = isBookmark,
                ).let(navigator::navigate)
            },
            navigateToTemplateCreate = { navigator.navigate(TemplateCreateNavKey) },
        )
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
