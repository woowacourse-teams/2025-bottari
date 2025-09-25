package com.bottari.presentation.compose.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ComposeHomeActivity::class.java)
    }
}
