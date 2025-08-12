package com.bottari.presentation.view.edit.team.management

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamManagementBinding
import com.bottari.presentation.view.edit.team.management.adapter.TeamMemberAdapter

class TeamManagementFragment :
    BaseFragment<FragmentTeamManagementBinding>(
        FragmentTeamManagementBinding::inflate,
    ) {
    private val viewModel: TeamManagementViewModel by viewModels {
        TeamManagementViewModel.Factory(requireArguments().getLong(ARG_TEAM_BOTTARI_ID))
    }
    private lateinit var adapter: TeamMemberAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter = TeamMemberAdapter(uiState.hostName)
            adapter.submitList(uiState.nonDuplicateNicknames)
            binding.tvTeamMemberHeadCount.text =
                getString(
                    R.string.team_management_member_head_count,
                    uiState.teamMemberHeadCount,
                    uiState.maxHeadCount,
                )
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is TeamManagementUiEvent.FetchTeamMembersFailure -> requireView().showSnackbar(R.string.team_management_fetch_failure_text)
            }
        }
    }

    private fun setupListener() {
        binding.btnClipboard.setOnClickListener { }
    }

    companion object {
        private const val ARG_TEAM_BOTTARI_ID = "ARG_TEAM_BOTTARI_ID"

        @JvmStatic
        fun newInstance(id: Long) =
            TeamManagementFragment().apply {
                arguments = bundleOf(ARG_TEAM_BOTTARI_ID to id)
            }
    }
}
