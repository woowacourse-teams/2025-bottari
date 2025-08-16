package com.bottari.presentation.view.checklist.team.status

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamStatusBinding
import com.bottari.presentation.model.TeamProductStatusUiModel
import com.bottari.presentation.view.checklist.team.status.adapter.TeamProductStatusAdapter
import com.bottari.presentation.view.checklist.team.status.adapter.TeamProductStatusDetailAdapter
import com.bottari.presentation.view.checklist.team.status.adapter.TeamProductStatusViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TeamStatusFragment :
    BaseFragment<FragmentTeamStatusBinding>(FragmentTeamStatusBinding::inflate),
    TeamProductStatusViewHolder.OnTeamProductStatusItemClickListener {
    private val viewModel: TeamStatusViewModel by viewModels {
        TeamStatusViewModel.Factory(requireArguments().getLong(ARG_BOTTARI_ID))
    }

    private val teamProductStatusDetailAdapter: TeamProductStatusDetailAdapter by lazy {
        TeamProductStatusDetailAdapter()
    }

    private val teamProductStatusAdapter: TeamProductStatusAdapter by lazy {
        TeamProductStatusAdapter(this)
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
            teamProductStatusDetailAdapter.submitList(state.item?.memberCheckStatus)
            binding.tvTeamBottariItemStatusTitle.text =
                getString(R.string.team_product_status_title_format, state.item?.name)
            teamProductStatusAdapter.submitList(state.teamChecklistItems)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                TeamStatusUiEvent.FetchTeamStatusFailure -> requireView().showSnackbar(R.string.team_status_fetch_failure_text)
                TeamStatusUiEvent.SendRemindSuccess -> requireView().showSnackbar(R.string.team_status_remind_success_text)
                TeamStatusUiEvent.SendRemindFailure -> requireView().showSnackbar(R.string.team_status_remind_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvTeamBottariItemStatusDetail.adapter = teamProductStatusDetailAdapter
        binding.rvTeamBottariItemStatusDetail.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
        binding.rvTeamBottariItems.adapter = teamProductStatusAdapter
        binding.rvTeamBottariItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.btnTeamBottariItemRemind.setOnClickListener {
            viewModel.remindTeamBottariItem()
        }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamStatusFragment =
            TeamStatusFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }

    override fun onItemClick(item: TeamProductStatusUiModel) {
        viewModel.selectItem(item)
        teamProductStatusAdapter.updateSelectedPosition(
            teamProductStatusAdapter.currentList.indexOf(
                item,
            ),
        )
    }
}
