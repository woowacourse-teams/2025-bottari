package com.bottari.presentation.view.team.checklist

import android.os.Bundle
import android.view.View
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentMemberStatusBinding

class MemberStatusFragment : BaseFragment<FragmentMemberStatusBinding>(FragmentMemberStatusBinding::inflate) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): MemberStatusFragment = MemberStatusFragment()
    }
}
