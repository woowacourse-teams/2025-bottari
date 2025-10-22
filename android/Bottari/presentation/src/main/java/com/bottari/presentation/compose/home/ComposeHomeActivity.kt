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
import com.bottari.presentation.compose.edit.personal.ComposePersonalBottariEditActivity
import com.bottari.presentation.compose.personal.ComposePersonalChecklistActivity
import com.bottari.presentation.compose.team.ComposeTeamChecklistActivity
import com.bottari.presentation.view.edit.team.TeamBottariEditActivity
import com.bottari.presentation.view.template.TemplateActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(BottariStatusBarStyle)
        setContent {
            BottariTheme {
                HomeScreen(
                    navigateToPersonalBottariEdit = ::navigateToPersonalBottariEdit,
                    navigateToTeamBottariEdit = ::navigateToTeamBottariEdit,
                    navigateToPersonalBottariChecklist = ::navigateToPersonalBottariChecklist,
                    navigateToTeamBottariChecklist = ::navigateToTeamBottariChecklist,
                    navigateToBrowser = ::navigateToBrowser,
                    navigateToTemplateDetail = ::navigateToTemplateDetail,
                    navigateToTemplateCreate = ::navigateToTemplateCreate,
                )
            }
        }
    }

    private fun navigateToBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    private fun navigateToTemplateDetail(templateId: Long) {
        val intent = TemplateActivity.newIntentForDetail(this, templateId)
        startActivity(intent)
    }

    private fun navigateToTemplateCreate() {
        val intent = TemplateActivity.newIntentForCreateTemplate(this)
        startActivity(intent)
    }

    private fun navigateToPersonalBottariEdit(
        bottariId: Long,
        isNew: Boolean,
    ) {
        val intent = ComposePersonalBottariEditActivity.newIntent(this, bottariId, isNew)
        startActivity(intent)
    }

    private fun navigateToTeamBottariEdit(
        bottariId: Long,
        isNew: Boolean,
    ) {
        val intent = TeamBottariEditActivity.newIntent(this, bottariId, isNew)
        startActivity(intent)
    }

    private fun navigateToPersonalBottariChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent = ComposePersonalChecklistActivity.newIntent(this, bottariId, bottariTitle)
        startActivity(intent)
    }

    private fun navigateToTeamBottariChecklist(
        bottariId: Long,
        bottariTitle: String,
    ) {
        val intent = ComposeTeamChecklistActivity.newIntent(this, bottariId, bottariTitle)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, ComposeHomeActivity::class.java)
    }
}
