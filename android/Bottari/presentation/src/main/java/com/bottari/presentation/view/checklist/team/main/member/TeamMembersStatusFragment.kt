package com.bottari.presentation.view.checklist.team.main.member

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamMembersStatusBinding
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel
import com.bottari.presentation.view.checklist.team.main.member.adapter.TeamMemberStatusAdapter
import com.bottari.presentation.view.checklist.team.main.member.adapter.TeamMemberStatusViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamMembersStatusFragment :
    BaseFragment<FragmentTeamMembersStatusBinding>(
        FragmentTeamMembersStatusBinding::inflate,
    ),
    TeamMemberStatusViewHolder.MemberStatusClickListener {
    private val viewModel: TeamMembersStatusViewModel by viewModels()
    private val adapter: TeamMemberStatusAdapter by lazy { TeamMemberStatusAdapter(this) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    override fun onClickMember(id: Long) {
        viewModel.updateExpandState(id)
    }

    override fun onClickSendRemind(member: TeamMemberUiModel) {
        viewModel.debouncedSendRemindMessage(member)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            adapter.submitList(uiState.membersStatus)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TeamMembersStatusUiEvent.FetchMembersStatusFailure ->
                    requireView().showSnackbar(R.string.team_members_status_fetch_failure_text)

                is TeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess ->
                    requireView().showSnackbar(
                        getString(
                            R.string.team_members_status_send_remind_message_success_text,
                            uiEvent.nickname,
                        ),
                    )

                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure ->
                    requireView().showSnackbar(R.string.team_members_status_send_remind_message_failure_text)

                TeamMembersStatusUiEvent.FetchMemberIdFailure ->
                    requireView().showSnackbar(R.string.team_members_status_fetch_member_id_failure_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvMembersStatus.adapter = adapter
        binding.rvMembersStatus.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            TeamMembersStatusFragment().apply {
                arguments = bundleOf(TeamMembersStatusViewModel.KEY_BOTTARI_ID to id)
            }
    }
}
