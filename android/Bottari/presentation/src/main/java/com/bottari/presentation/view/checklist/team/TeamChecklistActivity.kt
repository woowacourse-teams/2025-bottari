package com.bottari.presentation.view.checklist.team

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.checklist.TeamChecklistFragment
import com.google.android.material.tabs.TabLayoutMediator

class TeamChecklistActivity : BaseActivity<ActivityTeamChecklistBinding>(ActivityTeamChecklistBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val bottariId = intent.getLongExtra(EXTRA_BOTTARI_ID, -1)
        val bottariTitle = intent.getStringExtra(EXTRA_BOTTARI_TITLE)
        val adapter = TeamChecklistFragmentAdapter(this, bottariId)
        binding.tvBottariTitle.text = bottariTitle
        binding.vpTeamBottari.adapter = adapter

        TabLayoutMediator(binding.tlTeamBottari, binding.vpTeamBottari) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.tap_checklist)
                    1 -> getString(R.string.tap_team_current)
                    else -> getString(R.string.tap_member_checklist)
                }
        }.attach()
    }

    private class TeamChecklistFragmentAdapter(
        fa: FragmentActivity,
        private val bottariId: Long,
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> TeamChecklistFragment.newInstance(bottariId)
                1 -> TeamStatusFragment.newInstance()
                else -> MemberStatusFragment.newInstance()
            }
    }

    companion object {
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
