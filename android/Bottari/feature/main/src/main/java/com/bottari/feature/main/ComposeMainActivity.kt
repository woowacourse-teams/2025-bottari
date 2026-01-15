package com.bottari.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.bottari.designsystem.theme.LocalBottariBgColor
import com.bottari.core.navigation.FeatureNavKey
import com.bottari.core.navigation.LocalNavigator
import com.bottari.core.navigation.MainTabNavKey
import com.bottari.core.navigation.Navigator
import com.bottari.core.navigation.rememberNavigationState
import com.bottari.core.navigation.toEntries
import com.bottari.core.ui.provider.LocalSnackbarHostState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BottariTheme {
                App(
                    entryBuilders = entryBuilders,
                    startKey = intent.getNavKey(),
                )
            }
        }
    }

    private fun Intent.getNavKey(): NavKey {
        val inviteCode = getStringExtra(EXTRA_INVITE_CODE)
        if (inviteCode?.isNotBlank() == true) {
            return FeatureNavKey.InviteNavKey(inviteCode = inviteCode)
        }

        val personalBottariId = getLongExtra(EXTRA_PERSONAL_BOTTARI_ID, -1L)
        if (personalBottariId != -1L) {
            return FeatureNavKey.PersonalChecklistNavKey(
                bottariId = personalBottariId,
                bottariTitle = getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty(),
                notificationFlag = true,
            )
        }

        val teamBottariId = getLongExtra(EXTRA_TEAM_BOTTARI_ID, -1L)
        if (teamBottariId != -1L) {
            return FeatureNavKey.TeamChecklistNavKey(
                bottariId = teamBottariId,
                bottariTitle = getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty(),
                notificationFlag = true,
            )
        }

        return MainTabNavKey.MyBottariNavKey
    }

    companion object {
        private const val EXTRA_INVITE_CODE = "EXTRA_INVITE_CODE"
        private const val EXTRA_PERSONAL_BOTTARI_ID = "EXTRA_PERSONAL_BOTTARI_ID"
        private const val EXTRA_TEAM_BOTTARI_ID = "EXTRA_TEAM_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"

        fun newIntent(context: Context): Intent = Intent(context, ComposeMainActivity::class.java)

        fun newIntentForInvite(
            context: Context,
            inviteCode: String,
        ): Intent =
            newIntent(context).apply {
                putExtra(EXTRA_INVITE_CODE, inviteCode)
            }

        fun newIntentForPersonalChecklist(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            newIntent(context).putExtras(
                bundleOf(
                    EXTRA_PERSONAL_BOTTARI_ID to bottariId,
                    EXTRA_BOTTARI_TITLE to bottariTitle,
                ),
            )

        fun newIntentForTeamChecklist(
            context: Context,
            teamBottariId: Long,
            teamBottariTitle: String,
        ) = newIntent(context).putExtras(
            bundleOf(
                EXTRA_TEAM_BOTTARI_ID to teamBottariId,
                EXTRA_BOTTARI_TITLE to teamBottariTitle,
            ),
        )
    }
}

@Composable
fun App(
    entryBuilders: Set<EntryProviderScope<NavKey>.() -> Unit>,
    startKey: NavKey,
) {
    val navigationState = rememberNavigationState(startKey, TOP_LEVEL_NAV_ITEMS.keys)
    val navigator = remember { Navigator(navigationState) }
    val snackbarState = remember { SnackbarHostState() }

    val entryProvider =
        entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalSnackbarHostState provides snackbarState,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarState) },
            bottomBar = {
                if (navigationState.currentKey in TOP_LEVEL_NAV_ITEMS.keys) {
                    MainBottomNavigation(
                        selectedTab = navigationState.currentKey,
                        onTabSelected = navigator::navigate,
                    )
                }
            },
            containerColor = LocalBottariBgColor.current,
        ) { innerPadding ->
            NavDisplay(
                entries = navigationState.toEntries(entryProvider),
                onBack = navigator::goBack,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
