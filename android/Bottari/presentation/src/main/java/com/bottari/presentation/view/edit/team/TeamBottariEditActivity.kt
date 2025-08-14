package com.bottari.presentation.view.edit.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.commit
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.ActivityTeamBottariEditBinding
import com.bottari.presentation.view.edit.team.main.TeamBottariEditFragment
import com.bottari.presentation.view.edit.team.management.TeamManagementFragment

class TeamBottariEditActivity :
    BaseActivity<ActivityTeamBottariEditBinding>(
        ActivityTeamBottariEditBinding::inflate,
    ),
    TeamBottariEditNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) return
        showWelcomeMessage()
        setupUI()
        setupListener()
    }

    override fun navigateBack() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
            return
        }
        supportFragmentManager.popBackStack()
    }

    override fun navigateToMemberEdit(teamBottariId: Long) {
        supportFragmentManager.commit {
            replace(R.id.fcv_team_edit, TeamManagementFragment.newInstance(teamBottariId))
            addToBackStack(null)
        }
    }

    private fun setupUI() {
        navigateToScreen()
    }

    private fun setupListener() {
        onBackPressedDispatcher.addCallback(this) { navigateBack() }
    }

    private fun navigateToScreen() {
        supportFragmentManager.commit {
            val bottariId = getBottariIdFromIntent()
            replace(
                R.id.fcv_team_edit,
                TeamBottariEditFragment.newInstance(bottariId),
            )
        }
    }

    private fun getBottariIdFromIntent(): Long = intent?.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun showWelcomeMessage() {
        if (!intent.getBooleanExtra(EXTRA_IS_NEW_BOTTARI, false)) return
        binding.root.showSnackbar(R.string.bottari_create_success_text)
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_IS_NEW_BOTTARI = "EXTRA_IS_NEW_BOTTARI"

        fun newIntent(
            context: Context,
            bottariId: Long,
            isNewBottari: Boolean,
        ): Intent =
            Intent(context, TeamBottariEditActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_IS_NEW_BOTTARI, isNewBottari)
            }
    }
}
