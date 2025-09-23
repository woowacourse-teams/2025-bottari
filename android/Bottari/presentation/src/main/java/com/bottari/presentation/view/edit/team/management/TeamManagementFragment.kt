package com.bottari.presentation.view.edit.team.management

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamManagementBinding
import com.bottari.presentation.util.DeeplinkHelper.createDeeplink
import com.bottari.presentation.view.edit.team.management.adapter.TeamMemberAdapter

class TeamManagementFragment :
    BaseFragment<FragmentTeamManagementBinding>(
        FragmentTeamManagementBinding::inflate,
    ) {
    private val viewModel: TeamManagementViewModel by viewModels {
        TeamManagementViewModel.Factory(
            requireArguments().getLong(ARG_TEAM_BOTTARI_ID),
        )
    }
    private val adapter: TeamMemberAdapter by lazy { TeamMemberAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchTeamMembers()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.members)
            binding.tvTeamMemberHeadCount.text =
                getString(
                    R.string.team_management_member_head_count,
                    uiState.teamMemberHeadCount,
                    uiState.maxHeadCount,
                )
            binding.btnShare.isEnabled =
                uiState.inviteCode.isNotBlank()
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is TeamManagementUiEvent.FetchTeamMembersFailure -> requireView().showSnackbar(R.string.team_management_fetch_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvMemberList.adapter = adapter
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.btnShare.isEnabled = false
    }

    private fun setupListener() {
        binding.clAddTeamMember.setOnClickListener {
            shareInvite()
        }
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun shareInvite() {
        val state =
            viewModel.uiState.value ?: run {
                requireView().showSnackbar(R.string.team_management_share_failure_text)
                return
            }
        val inviteCode = state.inviteCode
        val bottariName = requireArguments().getString(ARG_TEAM_BOTTARI_NAME)
        val inviteLink = createDeeplink(inviteCode)
        val shareMessage =
            getString(
                R.string.team_management_share_template_text,
                bottariName,
                inviteCode,
                inviteLink,
            )
        val sendIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        private const val ARG_TEAM_BOTTARI_ID = "ARG_TEAM_BOTTARI_ID"
        private const val ARG_TEAM_BOTTARI_NAME = "ARG_TEAM_BOTTARI_NAME"

        @JvmStatic
        fun newInstance(
            id: Long,
            teamBottariName: String,
        ) = TeamManagementFragment().apply {
            arguments =
                bundleOf(ARG_TEAM_BOTTARI_ID to id, ARG_TEAM_BOTTARI_NAME to teamBottariName)
        }
    }
}
