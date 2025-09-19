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

class TeamMembersStatusFragment :
    BaseFragment<FragmentTeamMembersStatusBinding>(
        FragmentTeamMembersStatusBinding::inflate,
    ),
    TeamMemberStatusViewHolder.MemberStatusClickListener {
    private val viewModel: TeamMembersStatusViewModel by viewModels {
        TeamMembersStatusViewModel.Factory(requireArguments().getLong(ARG_TEAM_BOTTARI_ID))
    }
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
                is TeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess ->
                    requireView().showSnackbar(
                        getString(
                            R.string.team_members_status_send_remind_message_success_text,
                            uiEvent.nickname,
                        ),
                    )

                TeamMembersStatusUiEvent.FetchMembersStatusFailure.PermissionException,
                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure.PermissionException ->
                    requireView().showSnackbar("우리 보따리 접근 권한이 없어요")

                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure.DuplicatedException ->
                    requireView().showSnackbar("해당 팀원은 이미 물건을 다 챙겼어요")

                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure.InvalidException ->
                    requireView().showSnackbar("보챌 수 없는 팀원이에요")

                TeamMembersStatusUiEvent.FetchMemberIdFailure.NotFoundException,
                TeamMembersStatusUiEvent.FetchMembersStatusFailure.NotFoundException,
                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure.NotFoundException,
                -> requireView().showSnackbar("사용자 인증에 실패했어요")

                TeamMembersStatusUiEvent.FetchMemberIdFailure.UnexpectedException,
                TeamMembersStatusUiEvent.FetchMembersStatusFailure.UnexpectedException,
                TeamMembersStatusUiEvent.SendRemindByMemberMessageFailure.UnexpectedException,
                -> requireView().showSnackbar(R.string.common_unexpected_exception_text)
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
