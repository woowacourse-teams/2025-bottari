package com.bottari.presentation.view.edit.team.management

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter: TeamMemberAdapter by lazy { TeamMemberAdapter() }
    private val clipboardManager: ClipboardManager by lazy {
        requireContext().getSystemService(
            ClipboardManager::class.java,
        )
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

    override fun onStart() {
        super.onStart()
        viewModel.fetchTeamMembers()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.members)
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

    private fun setupUI() {
        binding.rvMemberList.adapter = adapter
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListener() {
        binding.btnClipboard.setOnClickListener { copyInviteCode() }
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun copyInviteCode() {
        val inviteCode =
            viewModel.uiState.value?.inviteCode ?: run {
                requireView().showSnackbar(R.string.team_management_copy_invite_code_success_text)
                return
            }
        val clip = ClipData.newPlainText(LABEL_INVITE_CODE, inviteCode)
        clipboardManager.setPrimaryClip(clip)
        requireView().showSnackbar(R.string.team_management_copy_invite_code_failure_text)
    }

    companion object {
        private const val ARG_TEAM_BOTTARI_ID = "ARG_TEAM_BOTTARI_ID"
        private const val LABEL_INVITE_CODE = "LABEL_INVITE_CODE"

        @JvmStatic
        fun newInstance(id: Long) =
            TeamManagementFragment().apply {
                arguments = bundleOf(ARG_TEAM_BOTTARI_ID to id)
            }
    }
}
