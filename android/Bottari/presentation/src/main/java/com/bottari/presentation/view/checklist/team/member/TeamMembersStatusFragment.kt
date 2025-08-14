package com.bottari.presentation.view.checklist.team.member

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamMembersStatusBinding
import com.bottari.presentation.view.checklist.team.member.adapter.TeamMemberStatusAdapter

class TeamMembersStatusFragment :
    BaseFragment<FragmentTeamMembersStatusBinding>(
        FragmentTeamMembersStatusBinding::inflate,
    ) {
    private val viewModel: TeamMembersStatusViewModel by viewModels {
        TeamMembersStatusViewModel.Factory(requireArguments().getLong(ARG_TEAM_BOTTARI_ID))
    }
    private val adapter: TeamMemberStatusAdapter by lazy { TeamMemberStatusAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchTeamMembersStatus()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.membersStatus)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is TeamMembersStatusUiEvent.FetchMembersStatusFailure ->
                    requireView().showSnackbar(
                        R.string.team_members_status_fetch_failure_text,
                    )
            }
        }
    }

    private fun setupUI() {
        binding.rvMembersStatus.adapter = adapter
        binding.rvMembersStatus.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        private const val ARG_TEAM_BOTTARI_ID = "ARG_TEAM_BOTTARI_ID"

        @JvmStatic
        fun newInstance(id: Long) =
            TeamMembersStatusFragment().apply {
                arguments = bundleOf(ARG_TEAM_BOTTARI_ID to id)
            }
    }
}
