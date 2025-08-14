package com.bottari.presentation.view.checklist.team

import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentTeamStatusBinding

class TeamStatusFragment : BaseFragment<FragmentTeamStatusBinding>(FragmentTeamStatusBinding::inflate) {
    companion object {
        @JvmStatic
        fun newInstance(): TeamStatusFragment = TeamStatusFragment()
    }
}
