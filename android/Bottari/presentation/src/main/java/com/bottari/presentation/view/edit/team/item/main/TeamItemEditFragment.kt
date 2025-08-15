package com.bottari.presentation.view.edit.team.item.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.databinding.FragmentTeamBottariItemEditBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.view.edit.team.TeamBottariEditNavigator
import com.bottari.presentation.view.edit.team.item.main.adapter.TeamItemEditFragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TeamItemEditFragment : BaseFragment<FragmentTeamBottariItemEditBinding>(FragmentTeamBottariItemEditBinding::inflate) {
    private val adapter: TeamItemEditFragmentAdapter by lazy {
        TeamItemEditFragmentAdapter(this, requireArguments().getLong(ARG_BOTTARI_ID))
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    private fun setupObserver() {}

    private fun setupUI() {
        binding.vpTeamBottariItemEdit.adapter = adapter
        binding.tlTeamBottariItemEdit
        TabLayoutMediator(
            binding.tlTeamBottariItemEdit,
            binding.vpTeamBottariItemEdit,
            ::setupTabs,
        ).attach()
        setRequireTab()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            (requireActivity() as? TeamBottariEditNavigator)?.navigateBack()
        }
    }

    private fun setRequireTab() {
        val tabType = requireArguments().getParcelableCompat<BottariItemTypeUiModel>(ARG_TAB_TYPE)
        val tabPos =
            when (tabType) {
                BottariItemTypeUiModel.SHARED -> 0
                is BottariItemTypeUiModel.ASSIGNED -> 1
                BottariItemTypeUiModel.PERSONAL -> 2
            }
        binding.vpTeamBottariItemEdit.currentItem = tabPos
    }

    private fun setupTabs(
        tab: TabLayout.Tab,
        position: Int,
    ) = when (position) {
        0 -> getString(R.string.bottari_item_type_shared_text)
        1 -> getString(R.string.bottari_item_type_assigned_text)
        2 -> getString(R.string.bottari_item_type_personal_text)
        else -> throw IllegalArgumentException(ERROR_UNKNOWN_TYPE)
    }.also { tabName -> tab.text = tabName }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val ARG_TAB_TYPE = "ARG_TAB_TYPE"
        private const val ERROR_UNKNOWN_TYPE = "[ERROR] 알 수 없는 타입입니다"

        fun newInstance(
            bottariId: Long,
            requireTabType: BottariItemTypeUiModel,
        ): TeamItemEditFragment =
            TeamItemEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId, ARG_TAB_TYPE to requireTabType)
            }
    }
}
