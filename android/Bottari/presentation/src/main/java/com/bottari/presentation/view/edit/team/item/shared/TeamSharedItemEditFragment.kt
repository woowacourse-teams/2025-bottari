package com.bottari.presentation.view.edit.team.item.shared

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamSharedItemEditBinding
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiEvent
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiState
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditViewModel
import com.bottari.presentation.view.edit.team.item.shared.adapter.TeamSharedItemEditAdapter

class TeamSharedItemEditFragment :
    BaseFragment<FragmentTeamSharedItemEditBinding>(FragmentTeamSharedItemEditBinding::inflate),
    TeamSharedItemEditAdapter.TeamSharedItemEditEventListener {
    private val parentViewModel: TeamItemEditViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
    )
    private val viewModel: TeamSharedItemEditViewModel by viewModels {
        TeamSharedItemEditViewModel.Factory(
            requireArguments().getLong(ARG_BOTTARI_ID),
        )
    }
    private val adapter: TeamSharedItemEditAdapter by lazy { TeamSharedItemEditAdapter(this) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    override fun onClickDelete(itemId: Long) {
        viewModel.deleteItem(itemId)
    }

    private fun setupObserver() {
        parentViewModel.uiEvent.observe(viewLifecycleOwner, ::handleParentUiEvent)
        parentViewModel.uiState.observe(viewLifecycleOwner, ::handleParentUiState)
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun setupUI() {
        binding.rvTeamSharedItemEdit.adapter = adapter
        binding.rvTeamSharedItemEdit.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleParentUiState(uiState: TeamItemEditUiState) {
        viewModel.updateInput(uiState.itemInputText)
    }

    private fun handleParentUiEvent(uiEvent: TeamItemEditUiEvent) {
        if (uiEvent !is TeamItemEditUiEvent.CreateTeamSharedItem) return
        viewModel.createItem()
    }

    private fun handleUiState(uiState: TeamSharedItemEditUiState) {
        toggleLoadingIndicator(uiState.isLoading)
        parentViewModel.updateIsAlreadyExistState(uiState.isAlreadyExist)
        binding.emptyView.root.isVisible = uiState.isEmpty
        adapter.submitList(uiState.personalItems)
    }

    private fun handleUiEvent(uiEvent: TeamSharedItemEditEvent) {
        when (uiEvent) {
            TeamSharedItemEditEvent.FetchTeamPersonalItemsFailure -> requireView().showSnackbar(R.string.common_fetch_failure_text)
            TeamSharedItemEditEvent.AddItemFailure -> requireView().showSnackbar(R.string.common_save_failure_text)
            TeamSharedItemEditEvent.DeleteItemFailure -> requireView().showSnackbar(R.string.common_save_failure_text)
        }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamSharedItemEditFragment =
            TeamSharedItemEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
