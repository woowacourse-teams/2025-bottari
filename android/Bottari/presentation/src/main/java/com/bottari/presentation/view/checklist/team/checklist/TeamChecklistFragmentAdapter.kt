package com.bottari.presentation.view.checklist.team.checklist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.view.checklist.team.MemberStatusFragment
import com.bottari.presentation.view.checklist.team.TeamStatusFragment

class TeamChecklistFragmentAdapter(
    fragmentActivity: FragmentActivity,
    private val bottariId: Long,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> TeamChecklistFragment.newInstance(bottariId)
            1 -> TeamStatusFragment.newInstance()
            else -> MemberStatusFragment.newInstance()
        }
}
