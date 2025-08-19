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
    private enum class Page {
        CHECKLIST,
        BOTTARI_STATUS,
        MEMBERS_STATUS,
    }

    override fun getItemCount(): Int = Page.entries.size

    override fun createFragment(position: Int): Fragment =
        when (Page.entries.getOrNull(position)) {
            Page.CHECKLIST -> TeamChecklistFragment.newInstance(bottariId)
            Page.BOTTARI_STATUS -> TeamBottariStatusFragment.newInstance(bottariId)
            Page.MEMBERS_STATUS -> TeamMembersStatusFragment.newInstance(bottariId)
            null -> throw IllegalArgumentException(ERROR_UNKNOWN_FRAGMENT)
        }

    companion object {
        private const val ERROR_UNKNOWN_FRAGMENT = "[ERROR] 알 수 없는 프래그먼트입니다."
    }
}
