package com.bottari.presentation.view.checklist.team.main.checklist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamChecklistBinding
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.view.checklist.team.main.checklist.adapter.TeamChecklistItemAdapter

class TeamChecklistFragment :
    BaseFragment<FragmentTeamChecklistBinding>(FragmentTeamChecklistBinding::inflate),
    TeamChecklistItemAdapter.TeamChecklistEventListener {
    private val viewModel: TeamChecklistViewModel by activityViewModels {
        TeamChecklistViewModel.Factory(requireArguments().getLong(ARG_BOTTARI_ID))
    }

    private val checklistAdapter: TeamChecklistItemAdapter by lazy {
        TeamChecklistItemAdapter(this)
    }

    override fun onItemTypeHeaderClick(type: BottariItemTypeUiModel) {
        viewModel.toggleParentExpanded(type)
    }

    override fun onTeamChecklistItemClick(
        id: Long,
        type: BottariItemTypeUiModel,
    ) {
        viewModel.toggleItemChecked(id, type)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            checklistAdapter.submitList(uiState.expandableItems)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TeamChecklistUiEvent.CheckItemFailure.DuplicatedException ->
                    requireView().showSnackbar("현재 체크 상태와 동일한 요청이예요")

                TeamChecklistUiEvent.FetchChecklistFailure.PermissionException ->
                    requireView().showSnackbar("우리 보따리 접근 권한이 없어요")

                TeamChecklistUiEvent.CheckItemFailure.NotFoundException,
                TeamChecklistUiEvent.FetchChecklistFailure.NotFoundException,
                -> requireView().showSnackbar("사용자 인증에 실패했어요")

                TeamChecklistUiEvent.CheckItemFailure.UnexpectedException,
                TeamChecklistUiEvent.FetchChecklistFailure.UnexpectedException,
                -> requireView().showSnackbar(R.string.common_unexpected_exception_text)
            }
        }
    }

    private fun setupUI() {
        binding.rvChecklist.adapter = checklistAdapter
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamChecklistFragment =
            TeamChecklistFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
