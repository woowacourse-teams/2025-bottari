package com.bottari.feature.invite.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.ui.provider.LocalSnackbarHostState
import com.bottari.feature.invite.InviteRoute
import com.bottari.feature.mybottari.navigation.MyBottariNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.inviteEntryBuilder() {
    entry<InviteNavKey> { navKey ->
        val navigator = LocalNavigator.current

        InviteRoute(
            snackbarState = LocalSnackbarHostState.current,
            inviteCode = navKey.inviteCode,
            onFinished = { navigator.navigate(MyBottariNavKey) },
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
