package com.bottari.presentation.view.edit.team.item.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.view.edit.team.item.assigned.TeamAssignedItemEditFragment
import com.bottari.presentation.view.edit.team.item.personal.TeamPersonalItemEditFragment
import com.bottari.presentation.view.edit.team.item.shared.TeamSharedItemEditFragment

class TeamItemEditFragmentAdapter(
    fragment: Fragment,
    private val bottariId: Long,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> TeamSharedItemEditFragment.newInstance(bottariId)
            1 -> TeamAssignedItemEditFragment.newInstance(bottariId)
            2 -> TeamPersonalItemEditFragment.newInstance(bottariId)
            else -> throw IllegalArgumentException(ERROR_UNKNOWN_FRAGMENT)
        }

    companion object {
        private const val ERROR_UNKNOWN_FRAGMENT = "[ERROR] 알 수 없는 프래그먼트입니다"
    }
}
