package com.bottari.feature.template.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.FeatureNavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.template.TemplateScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.templateEntryBuilder() {
    entry<MainTabNavKey.TemplateNavKey> {
        val navigator = LocalNavigator.current

        TemplateScreen(
            snackbarState = LocalSnackbarHostState.current,
            navigateToTemplateDetail = { id, isMyTemplate, isBookmark ->
                FeatureNavKey
                    .TemplateDetailNavKey(
                        templateId = id,
                        isMyTemplate = isMyTemplate,
                        isBookmark = isBookmark,
                    ).let(navigator::navigate)
            },
            navigateToTemplateCreate = { navigator.navigate(FeatureNavKey.TemplateCreateNavKey) },
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
