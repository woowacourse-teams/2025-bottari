package com.bottari.presentation.view.checklist.team.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.view.checklist.team.main.checklist.TeamChecklistFragment
import com.bottari.presentation.view.checklist.team.main.member.TeamMembersStatusFragment
import com.bottari.presentation.view.checklist.team.main.status.TeamBottariStatusFragment

class TeamChecklistFragmentAdapter(
    fragment: Fragment,
    private val bottariId: Long,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> TeamChecklistFragment.newInstance(bottariId)
            1 -> TeamBottariStatusFragment.newInstance(bottariId)
            2 -> TeamMembersStatusFragment.newInstance(bottariId)
            else -> throw IllegalArgumentException(ERROR_UNKNOWN_FRAGMENT)
        }

    companion object {
        private const val ERROR_UNKNOWN_FRAGMENT = "[ERROR] 알 수 없는 프래그먼트입니다."
    }
}
