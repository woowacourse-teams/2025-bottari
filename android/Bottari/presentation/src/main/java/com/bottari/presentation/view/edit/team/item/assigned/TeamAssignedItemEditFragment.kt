package com.bottari.presentation.view.edit.team.item.assigned

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamAssignedItemEditBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.view.common.decoration.ItemSpacingDecoration
import com.bottari.presentation.view.edit.team.item.assigned.adapter.TeamAssignedItemEditAdapter
import com.bottari.presentation.view.edit.team.item.assigned.adapter.TeamAssignedItemEditMemberAdapter
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiEvent
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiState
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditViewModel

class TeamAssignedItemEditFragment :
    BaseFragment<FragmentTeamAssignedItemEditBinding>(FragmentTeamAssignedItemEditBinding::inflate),
    TeamAssignedItemEditAdapter.TeamAssignedItemEditEventListener,
    TeamAssignedItemEditMemberAdapter.TeamAssignedItemEditMemberEventListener {
    private val parentViewModel: TeamItemEditViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
    )
    private val viewModel: TeamAssignedItemEditViewModel by viewModels {
        TeamAssignedItemEditViewModel.Factory(
            requireArguments().getLong(ARG_BOTTARI_ID),
        )
    }
    private val itemAdapter: TeamAssignedItemEditAdapter by lazy { TeamAssignedItemEditAdapter(this) }
    private val memberAdapter: TeamAssignedItemEditMemberAdapter by lazy {
        TeamAssignedItemEditMemberAdapter(
            this,
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

    override fun onClickDelete(itemId: Long) {
        viewModel.deleteItem(itemId)
    }

    override fun onClickAssignedItem(
        itemName: String,
        itemId: Long,
    ) {
        viewModel.selectAssignedItem(itemId)
        parentViewModel.updateInput(itemName)
    }

    override fun onClickMember(memberId: Long) {
        viewModel.selectMember(memberId)
    }

    private fun setupObserver() {
        parentViewModel.createItemEvent.observe(viewLifecycleOwner, ::handleParentUiEvent)
        parentViewModel.uiState.observe(viewLifecycleOwner, ::handleParentUiState)
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun setupUI() {
        binding.rvTeamAssignedItemEdit.adapter = itemAdapter
        binding.rvTeamAssignedItemEdit.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTeamAssignedItemMember.adapter = memberAdapter
        binding.rvTeamAssignedItemMember.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTeamAssignedItemMember.addItemDecoration(
            ItemSpacingDecoration(
                requireContext().dpToPx(ITEM_SPACING_VALUE),
                true,
            ),
        )
    }

    private fun handleParentUiState(uiState: TeamItemEditUiState) {
        if (uiState.currentTabType !is BottariItemTypeUiModel.ASSIGNED) return
        viewModel.updateInput(uiState.itemInputText)
    }

    private fun handleParentUiEvent(uiEvent: TeamItemEditUiEvent?) {
        if (uiEvent !is TeamItemEditUiEvent.CreateTeamAssignedItem) return
        viewModel.createItem()
    }

    private fun handleUiState(uiState: TeamAssignedItemEditUiState) {
        toggleLoadingIndicator(uiState.isLoading)
        parentViewModel.updateSendCondition(uiState.sendCondition)
        binding.emptyView.root.isVisible = uiState.isEmpty
        itemAdapter.submitList(uiState.assignedItems)
        memberAdapter.submitList(uiState.members)
    }

    private fun handleUiEvent(uiEvent: TeamAssignedItemEditEvent) {
        when (uiEvent) {
            TeamAssignedItemEditEvent.FetchTeamAssignedItemsFailure -> requireView().showSnackbar(R.string.common_fetch_failure_text)
            TeamAssignedItemEditEvent.DeleteItemFailure -> requireView().showSnackbar(R.string.common_save_failure_text)
            TeamAssignedItemEditEvent.CreateItemFailure -> requireView().showSnackbar(R.string.common_save_failure_text)
            TeamAssignedItemEditEvent.CreateItemSuccess ->
                parentViewModel.updateInput(RESET_INPUT_TEXT)
        }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val RESET_INPUT_TEXT = ""
        private const val ITEM_SPACING_VALUE = 8

        fun newInstance(bottariId: Long): TeamAssignedItemEditFragment =
            TeamAssignedItemEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
