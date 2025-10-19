package com.bottari.presentation.compose.personal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bottari.presentation.compose.common.theme.BottariStatusBarStyle
import com.bottari.presentation.compose.common.theme.BottariTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposePersonalChecklistActivity : AppCompatActivity() {
    private val bottariTitle: String by lazy {
        intent.getStringExtra(EXTRA_BOTTARI_TITLE) ?: ""
    }

    private val notificationFlag: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_NOTIFICATION_FLAG, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            BottariTheme {
                PersonalBottariScreen(
                    bottariTitle = bottariTitle,
                    notificationFlag = notificationFlag,
                )
            }
        }
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"
        private const val EXTRA_NOTIFICATION_FLAG = "EXTRA_FLAG"

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ComposePersonalChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }

        fun newIntentForNotification(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, ComposePersonalChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
                putExtra(EXTRA_NOTIFICATION_FLAG, true)
            }
    }
}
