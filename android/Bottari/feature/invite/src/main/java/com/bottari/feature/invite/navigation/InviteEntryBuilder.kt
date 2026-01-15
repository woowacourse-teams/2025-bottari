package com.bottari.feature.invite.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.FeatureNavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.invite.InviteRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.inviteEntryBuilder() {
    entry<FeatureNavKey.InviteNavKey> { navKey ->
        val navigator = LocalNavigator.current

        InviteRoute(
            snackbarState = LocalSnackbarHostState.current,
            inviteCode = navKey.inviteCode,
            onFinished = { navigator.navigate(MainTabNavKey.MyBottariNavKey) },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object InviteEntryBuilderModule {
    @IntoSet
    @Provides
    fun provideInviteEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            inviteEntryBuilder()
        }
}
