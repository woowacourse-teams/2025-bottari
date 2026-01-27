package com.bottari.presentation.compose.template

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
class ComposeCreateTemplateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            BottariTheme {
                CreateTemplateScreen(navigateBack = ::finish)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ComposeCreateTemplateActivity::class.java)
    }
}
