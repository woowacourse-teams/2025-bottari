package com.bottari.presentation.compose.edit.personal

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
class ComposePersonalBottariEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            BottariTheme {
                PersonalBottariEditScreen(
                    onBackClick = { finish() },
                )
            }
        }
    }

    companion object {
        private const val EXTRA_IS_NEW_BOTTARI = "EXTRA_IS_NEW_BOTTARI"

        fun newIntent(
            context: Context,
            bottariId: Long,
            isNewBottari: Boolean,
        ): Intent =
            Intent(context, ComposePersonalBottariEditActivity::class.java).apply {
                putExtra(PersonalBottariEditViewModel.KEY_BOTTARI_ID, bottariId)
                putExtra(EXTRA_IS_NEW_BOTTARI, isNewBottari)
            }
    }
}
