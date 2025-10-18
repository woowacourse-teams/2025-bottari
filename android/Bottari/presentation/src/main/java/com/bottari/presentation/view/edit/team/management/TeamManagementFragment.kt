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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamManagementFragment :
    BaseFragment<FragmentTeamManagementBinding>(
        FragmentTeamManagementBinding::inflate,
    ) {
    private val viewModel: TeamManagementViewModel by viewModels()
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
            binding.btnShare.isEnabled = uiState.isInviteCodeValid
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
        val shareMessage = generateShareMessage(state.inviteCode)
        val sendIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun generateShareMessage(inviteCode: String): String {
        val bottariName = requireArguments().getString(ARG_TEAM_BOTTARI_TITLE)
        val inviteLink = createDeeplink(inviteCode)
        return getString(
            R.string.team_management_share_template_text,
            bottariName,
            inviteCode,
            inviteLink,
        )
    }

    companion object {
        private const val ARG_TEAM_BOTTARI_TITLE = "ARG_TEAM_BOTTARI_TITLE"

        @JvmStatic
        fun newInstance(
            id: Long,
            title: String,
        ) = TeamManagementFragment().apply {
            arguments =
                bundleOf(
                    TeamManagementViewModel.KEY_BOTTARI_ID to id,
                    ARG_TEAM_BOTTARI_TITLE to title,
                )
        }
    }
}
