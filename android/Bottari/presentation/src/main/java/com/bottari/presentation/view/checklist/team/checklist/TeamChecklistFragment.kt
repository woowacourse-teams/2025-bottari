package com.bottari.presentation.view.checklist.team.checklist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamChecklistBinding
import com.bottari.presentation.view.checklist.team.checklist.adapter.TeamChecklistItemAdapter

class TeamChecklistFragment : BaseFragment<FragmentTeamChecklistBinding>(FragmentTeamChecklistBinding::inflate) {
    private val viewModel: TeamChecklistViewModel by viewModels {
        TeamChecklistViewModel.Factory(requireArguments().getLong(ARG_BOTTARI_ID))
    }

    private val checklistAdapter: TeamChecklistItemAdapter by lazy {
        TeamChecklistItemAdapter(
            onParentClick = { parent ->
                viewModel.toggleParentExpanded(parent.category)
            },
            onChildClick = { item ->
                viewModel.toggleItemChecked(item.id, item.category)
            },
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun setupUI() {
        binding.rvChecklist.adapter = checklistAdapter
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            checklistAdapter.submitList(uiState.expandableItems)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            val message =
                when (uiEvent) {
                    TeamChecklistUiEvent.CheckItemFailure -> R.string.checklist_fetch_failure_text
                    TeamChecklistUiEvent.FetchChecklistFailure -> R.string.checklist_check_failure_text
                }
            requireView().showSnackbar(message)
        }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        @JvmStatic
        fun newInstance(bottariId: Long): TeamChecklistFragment =
            TeamChecklistFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
