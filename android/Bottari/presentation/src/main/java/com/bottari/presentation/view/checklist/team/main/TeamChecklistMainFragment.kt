package com.bottari.presentation.view.checklist.team.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentTeamChecklistMainBinding
import com.bottari.presentation.view.checklist.team.main.checklist.adapter.TeamChecklistFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TeamChecklistMainFragment : BaseFragment<FragmentTeamChecklistMainBinding>(FragmentTeamChecklistMainBinding::inflate) {
    private lateinit var adapter: TeamChecklistFragmentAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            TeamChecklistFragmentAdapter(
                requireActivity(),
                requireArguments().getLong(ARG_BOTTARI_ID),
            )
        binding.vpTeamChecklist.adapter = adapter
        TabLayoutMediator(binding.tlTeamChecklistMain, binding.vpTeamChecklist) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.team_checklist_tap_checklist_text)
                    1 -> getString(R.string.team_checklist_tap_team_current_text)
                    2 -> getString(R.string.team_checklist_tap_member_checklist_text)
                    else -> throw IllegalArgumentException(ERROR_UNKNOWN_TYPE)
                }
        }.attach()
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val ERROR_UNKNOWN_TYPE = "[ERROR] 알 수 없는 타입입니다."

        fun newInstance(bottariId: Long): TeamChecklistMainFragment =
            TeamChecklistMainFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
