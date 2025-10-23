package com.bottari.presentation.compose.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bottari.presentation.compose.common.theme.BottariStatusBarStyle
import com.bottari.presentation.compose.home.ComposeHomeActivity
import com.bottari.presentation.compose.team.checklist.ComposeTeamChecklistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeTeamChecklistActivity : AppCompatActivity() {
    private val notificationFlag: Boolean by lazy {
        intent.getBooleanExtra(
            EXTRA_NOTIFICATION_FLAG,
            false,
        )
    }

    private val bottariTitle: String by lazy {
        intent.getStringExtra(EXTRA_BOTTARI_TITLE).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            TeamBottariScreen(
                bottariTitle = bottariTitle,
                notificationFlag = notificationFlag,
                navigateBack = ::navigateToBackOrHome,
            )
        }
    }

    private fun navigateToBackOrHome() {
        if (!isTaskRoot) {
            finish()
            return
        }
        val intent = Intent(this, ComposeHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_NOTIFICATION_FLAG = "EXTRA_NOTIFICATION_FLAG"

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ComposeTeamChecklistActivity::class.java).apply {
                putExtra(ComposeTeamChecklistViewModel.KEY_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }

        fun newIntentForNotification(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ComposeTeamChecklistActivity::class.java).apply {
                putExtra(ComposeTeamChecklistViewModel.KEY_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
                putExtra(EXTRA_NOTIFICATION_FLAG, true)
            }
    }
}
