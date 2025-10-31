package com.bottari.presentation.compose.edit.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.bottari.presentation.compose.common.theme.BottariStatusBarStyle
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.team.main.TeamBottariEditScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeTeamBottariEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BottariTheme {
                TeamBottariEditScreen()
            }
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val EXTRA_IS_NEW_BOTTARI = "EXTRA_IS_NEW_BOTTARI"

        fun newIntent(
            context: Context,
            bottariId: Long,
            isNew: Boolean,
        ): Intent =
            Intent(context, ComposeTeamBottariEditActivity::class.java).apply {
                putExtra(KEY_BOTTARI_ID, bottariId)
                putExtra(EXTRA_IS_NEW_BOTTARI, isNew)
            }
    }
}
