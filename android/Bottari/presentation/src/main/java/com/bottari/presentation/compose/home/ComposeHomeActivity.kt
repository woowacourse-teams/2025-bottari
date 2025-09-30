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
import com.bottari.presentation.view.checklist.personal.ChecklistActivity
import com.bottari.presentation.view.checklist.team.TeamChecklistActivity
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.edit.team.TeamBottariEditActivity
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
                    navigateToPersonalBottariEdit = ::navigateToPersonalBottariEdit,
                    navigateToTeamBottariEdit = ::navigateToTeamBottariEdit,
                    navigateToPersonalBottariChecklist = ::navigateToPersonalBottariChecklist,
                    navigateToTeamBottariChecklist = ::navigateToTeamBottariChecklist,
                )
            }
        }
    }

    private fun navigateToBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    fun navigateToPersonalBottariEdit(
        bottariId: Long,
        isNew: Boolean,
    ) {
        val intent = PersonalBottariEditActivity.newIntent(this, bottariId, isNew)
        startActivity(intent)
    }

    fun navigateToTeamBottariEdit(
        bottariId: Long,
        isNew: Boolean,
    ) {
        val intent = TeamBottariEditActivity.newIntent(this, bottariId, isNew)
        startActivity(intent)
    }

    fun navigateToPersonalBottariChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent = ChecklistActivity.newIntent(this, bottariId, bottariTitle)
        startActivity(intent)
    }

    fun navigateToTeamBottariChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent = TeamChecklistActivity.newIntent(this, bottariId, bottariTitle)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ComposeHomeActivity::class.java)
    }
}
