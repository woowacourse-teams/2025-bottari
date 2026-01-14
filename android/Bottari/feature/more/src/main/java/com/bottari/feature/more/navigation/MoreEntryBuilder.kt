package com.bottari.feature.more.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.feature.more.MoreScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.moreEntryBuilder() {
    entry<MainTabNavKey.MoreNavKey> {
        val navigator = LocalNavigator.current
        val activity = LocalActivity.current

        MoreScreen(
            onNavigateToBrowser = { url -> activity?.openUrl(url) },
        )
    }
}

fun Activity.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
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
