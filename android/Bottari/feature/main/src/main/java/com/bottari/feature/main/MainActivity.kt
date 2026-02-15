package com.bottari.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.os.bundleOf
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.domain.network.NetworkManager
import com.bottari.feature.invite.navigation.InviteNavKey
import com.bottari.feature.mybottari.navigation.MyBottariNavKey
import com.bottari.feature.personal.checklist.navigation.PersonalChecklistNavKey
import com.bottari.feature.team.checklist.navigation.TeamChecklistNavKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkManager: NetworkManager

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
                    networkManager = networkManager,
                )
            }
        }
    }

    private fun Intent.getNavKey(): NavKey {
        val inviteCode = getStringExtra(EXTRA_INVITE_CODE)
        if (inviteCode?.isNotBlank() == true) {
            return InviteNavKey(inviteCode = inviteCode)
        }

        val personalBottariId = getLongExtra(EXTRA_PERSONAL_BOTTARI_ID, -1L)
        if (personalBottariId != -1L) {
            return PersonalChecklistNavKey(
                bottariId = personalBottariId,
                bottariTitle = getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty(),
                notificationFlag = true,
            )
        }

        val teamBottariId = getLongExtra(EXTRA_TEAM_BOTTARI_ID, -1L)
        if (teamBottariId != -1L) {
            return TeamChecklistNavKey(
                bottariId = teamBottariId,
                bottariTitle = getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty(),
                notificationFlag = true,
            )
        }

        return MyBottariNavKey
    }

    companion object Companion {
        private const val EXTRA_INVITE_CODE = "EXTRA_INVITE_CODE"
        private const val EXTRA_PERSONAL_BOTTARI_ID = "EXTRA_PERSONAL_BOTTARI_ID"
        private const val EXTRA_TEAM_BOTTARI_ID = "EXTRA_TEAM_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"

        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

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
