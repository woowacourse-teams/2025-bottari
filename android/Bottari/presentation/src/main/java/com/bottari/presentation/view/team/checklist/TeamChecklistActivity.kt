package com.bottari.presentation.view.team.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamChecklistBinding
import com.bottari.presentation.view.team.checklist.checklist.TeamChecklistFragment
import com.google.android.material.tabs.TabLayoutMediator

class TeamChecklistActivity : BaseActivity<ActivityTeamChecklistBinding>(ActivityTeamChecklistBinding::inflate) {
    private lateinit var teamChecklistFragment: TeamChecklistFragment
    private lateinit var teamStatusFragment: TeamStatusFragment
    private lateinit var memberStatusFragment: MemberStatusFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()
        setupViewPager()
    }

    private fun initFragments() {
        teamChecklistFragment = TeamChecklistFragment.newInstance(intent.getLongExtra(EXTRA_BOTTARI_ID, -1))
        teamStatusFragment = TeamStatusFragment()
        memberStatusFragment = MemberStatusFragment()
    }

    private fun setupViewPager() {
        val adapter = TeamChecklistFragmentAdapter(this)
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

    inner class TeamChecklistFragmentAdapter(
        fa: FragmentActivity,
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> teamChecklistFragment
                1 -> teamStatusFragment
                else -> memberStatusFragment
            }
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, TeamChecklistActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
