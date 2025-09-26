package com.bottari.presentation.compose.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bottari.presentation.compose.common.theme.BottariStatusBarStyle
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.view.template.TemplateActivity

class ComposeHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            BottariTheme {
                HomeScreen(
                    navigateToBrowser = { url ->
                        navigateToBrowser(url)
                    },
                    navigateToTemplateDetail = { templateId ->
                        val newIntent = TemplateActivity.newIntentForDetail(this, templateId)
                        startActivity(newIntent)
                    },
                    navigateToTemplateCreate = {
                        val newIntent = TemplateActivity.newIntentForCreateTemplate(this)
                        startActivity(newIntent)
                    },
                )
            }
        }
    }

    private fun navigateToBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ComposeHomeActivity::class.java)
    }
}
