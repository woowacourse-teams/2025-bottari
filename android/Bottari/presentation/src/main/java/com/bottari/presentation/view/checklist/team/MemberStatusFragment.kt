package com.bottari.presentation.view.checklist.team

import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.databinding.FragmentMemberStatusBinding

class MemberStatusFragment : BaseFragment<FragmentMemberStatusBinding>(FragmentMemberStatusBinding::inflate) {
    companion object {
        fun newInstance(): MemberStatusFragment = MemberStatusFragment()
    }
}
