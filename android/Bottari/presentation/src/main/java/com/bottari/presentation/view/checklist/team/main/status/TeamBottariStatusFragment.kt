package com.bottari.presentation.view.checklist.team.main.status

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamStatusBinding
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.view.checklist.team.main.status.adapter.TeamBottariProductStatusAdapter
import com.bottari.presentation.view.checklist.team.main.status.adapter.TeamBottariProductStatusDetailAdapter
import com.bottari.presentation.view.checklist.team.main.status.adapter.TeamBottariProductStatusViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TeamBottariStatusFragment :
    BaseFragment<FragmentTeamStatusBinding>(FragmentTeamStatusBinding::inflate),
    TeamBottariProductStatusViewHolder.OnTeamProductStatusItemClickListener {
    private val viewModel: TeamBottariStatusViewModel by viewModels {
        TeamBottariStatusViewModel.Factory(requireArguments().getLong(ARG_BOTTARI_ID))
    }

    private val teamBottariProductStatusDetailAdapter: TeamBottariProductStatusDetailAdapter by lazy {
        TeamBottariProductStatusDetailAdapter()
    }

    private val teamBottariProductStatusAdapter: TeamBottariProductStatusAdapter by lazy {
        TeamBottariProductStatusAdapter(this)
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

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.btnTeamBottariItemSendHurryUp.isVisible =
                state.selectedProduct?.isAllChecked?.not() ?: false
            teamBottariProductStatusDetailAdapter.submitList(state.selectedProduct?.memberCheckStatus)
            binding.tvTeamBottariItemStatusTitle.text = state.selectedProduct?.name ?: ""
            teamBottariProductStatusAdapter.submitList(state.teamChecklistItems)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                TeamBottariStatusUiEvent.FetchTeamBottariStatusFailure ->
                    requireView().showSnackbar(
                        R.string.team_product_status_fetch_failure_text,
                    )
                TeamBottariStatusUiEvent.SendRemindSuccess -> requireView().showSnackbar(R.string.team_status_send_remind_success_text)
                TeamBottariStatusUiEvent.SendRemindFailure -> requireView().showSnackbar(R.string.team_status_send_remind_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvTeamBottariItemStatusDetail.adapter = teamBottariProductStatusDetailAdapter
        binding.rvTeamBottariItemStatusDetail.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
        binding.rvTeamBottariItems.adapter = teamBottariProductStatusAdapter
        binding.rvTeamBottariItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.btnTeamBottariItemSendHurryUp.setOnClickListener {
            viewModel.debouncedSendRemindByItem(Unit)
        }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamBottariStatusFragment =
            TeamBottariStatusFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }

    override fun onItemClick(item: TeamBottariProductStatusUiModel) {
        viewModel.selectItem(item)
        teamBottariProductStatusAdapter.updateSelectedPosition(
            teamBottariProductStatusAdapter.currentList.indexOf(
                item,
            ),
        )
    }
}
