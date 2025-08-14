package com.bottari.presentation.view.checklist.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.checklist.TeamChecklistFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TeamChecklistActivity : BaseActivity<ActivityTeamChecklistBinding>(ActivityTeamChecklistBinding::inflate) {
    val bottariId: Long by lazy { intent.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID) }

    val adapter: TeamChecklistFragmentAdapter by lazy {
        TeamChecklistFragmentAdapter(this, bottariId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val bottariTitle = intent.getStringExtra(EXTRA_BOTTARI_TITLE)
        val adapter = TeamChecklistFragmentAdapter(this, bottariId)
        binding.tvBottariTitle.text = bottariTitle
        binding.vpTeamBottari.adapter = adapter

        TabLayoutMediator(binding.tlTeamBottari, binding.vpTeamBottari) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.team_checklist_tap_checklist_text)
                    1 -> getString(R.string.team_checklist_tap_team_current_text)
                    else -> getString(R.string.team_checklist_tap_member_checklist_text)
                }
        }.attach()
    }

    companion object {
        private const val INVALID_BOTTARI_ID = -1L

        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_TITLE = "EXTRA_BOTTARI_TITLE"

        fun newIntent(
            context: Context,
            bottariId: Long,
            bottariTitle: String,
        ): Intent =
            Intent(context, TeamChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
                putExtra(EXTRA_BOTTARI_TITLE, bottariTitle)
            }
    }
}
