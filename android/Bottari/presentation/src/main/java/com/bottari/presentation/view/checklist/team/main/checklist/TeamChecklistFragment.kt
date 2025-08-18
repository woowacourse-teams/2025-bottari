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
import com.bottari.presentation.model.BottariItemTypeUiModel
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

    override fun onClick(type: BottariItemTypeUiModel) {
        viewModel.toggleParentExpanded(type)
    }

    override fun onClick(
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
                TeamChecklistUiEvent.FetchChecklistFailure -> requireView().showSnackbar(R.string.checklist_fetch_failure_text)
                TeamChecklistUiEvent.CheckItemFailure -> requireView().showSnackbar(R.string.checklist_check_failure_text)
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
