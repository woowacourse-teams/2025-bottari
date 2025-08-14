package com.bottari.presentation.view.checklist.team

import android.os.Bundle
import android.view.View
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentTeamStatusBinding

class TeamStatusFragment : BaseFragment<FragmentTeamStatusBinding>(FragmentTeamStatusBinding::inflate) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TeamStatusFragment = TeamStatusFragment()
    }
}
