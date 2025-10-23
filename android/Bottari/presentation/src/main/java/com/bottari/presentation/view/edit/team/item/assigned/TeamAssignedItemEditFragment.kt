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
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.view.common.decoration.ItemSpacingDecoration
import com.bottari.presentation.view.edit.team.item.assigned.adapter.TeamAssignedItemEditAdapter
import com.bottari.presentation.view.edit.team.item.assigned.adapter.TeamAssignedItemEditMemberAdapter
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiEvent
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditUiState
import com.bottari.presentation.view.edit.team.item.main.TeamItemEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamAssignedItemEditFragment :
    BaseFragment<FragmentTeamAssignedItemEditBinding>(FragmentTeamAssignedItemEditBinding::inflate),
    TeamAssignedItemEditAdapter.TeamAssignedItemEditEventListener,
    TeamAssignedItemEditMemberAdapter.TeamAssignedItemEditMemberEventListener {
    private val parentViewModel: TeamItemEditViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
    )
    private val viewModel: TeamAssignedItemEditViewModel by viewModels()
    private val itemAdapter: TeamAssignedItemEditAdapter by lazy { TeamAssignedItemEditAdapter(this) }
    private val memberAdapter: TeamAssignedItemEditMemberAdapter by lazy {
        TeamAssignedItemEditMemberAdapter(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    override fun onClickAssignedItemDelete(itemId: Long) = viewModel.requestDeleteItem(itemId)

    override fun onClickAssignedItem(itemId: Long) = viewModel.toggleEditState(itemId)

    override fun onClickMember(memberId: Long) = viewModel.selectMember(memberId)

    private fun setupObserver() {
        parentViewModel.createItemEvent.observe(viewLifecycleOwner, ::handleParentUiEvent)
        parentViewModel.uiState.observe(viewLifecycleOwner, ::handleParentUiState)
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun setupUI() {
        setupRvMember()
        setupRvAssignedItem()
    }

    private fun handleParentUiState(uiState: TeamItemEditUiState) {
        if (uiState.currentTabType !is BottariItemTypeUiModel.ASSIGNED) {
            viewModel.saveSelectedState()
            parentViewModel.updateSendCondition(true)
            return
        }
        viewModel.updateInput(uiState.itemInputText)
    }

    private fun handleParentUiEvent(uiEvent: TeamItemEditUiEvent?) {
        if (uiEvent !is TeamItemEditUiEvent.CreateTeamAssignedItem) return
        viewModel.submitItem()
    }

    private fun handleUiState(uiState: TeamAssignedItemEditUiState) {
        toggleLoadingIndicator(uiState.isLoading)
        binding.emptyView.root.isVisible = uiState.isEmpty

        parentViewModel.updateSendCondition(uiState.canSend)
        parentViewModel.updateIsAlreadyExistState(uiState.isAlreadyExist)

        itemAdapter.submitList(uiState.assignedItems)
        memberAdapter.submitList(uiState.members)
    }

    private fun handleUiEvent(uiEvent: TeamAssignedItemEditEvent) {
        when (uiEvent) {
            is TeamAssignedItemEditEvent.SelectAssignedItem -> parentViewModel.updateInput(uiEvent.itemName)
            TeamAssignedItemEditEvent.FetchTeamAssignedItemsFailure ->
                requireView().showSnackbar(
                    messageRes = R.string.common_fetch_failure_text,
                )

            TeamAssignedItemEditEvent.DeleteItemFailure,
            TeamAssignedItemEditEvent.CreateItemFailure,
            TeamAssignedItemEditEvent.SaveItemFailure,
            ->
                requireView().showSnackbar(messageRes = R.string.common_save_failure_text)

            TeamAssignedItemEditEvent.SaveItemSuccess -> handleItemEditSuccess()
            TeamAssignedItemEditEvent.CreateItemSuccess,
            -> {
                handleItemEditSuccess()
                val target = (itemAdapter.itemCount - 1).coerceAtLeast(0)
                binding.rvTeamAssignedItemEdit.smoothScrollToPosition(target)
            }
        }
    }

    private fun setupRvAssignedItem() {
        binding.rvTeamAssignedItemEdit.apply {
            adapter = itemAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupRvMember() {
        val decoration = ItemSpacingDecoration(requireContext().dpToPx(ITEM_SPACING_VALUE), true)
        binding.rvTeamAssignedItemMember.apply {
            adapter = memberAdapter
            itemAnimator = null
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(decoration)
        }
    }

    private fun handleItemEditSuccess() {
        parentViewModel.updateInput(RESET_INPUT_TEXT)
    }

    companion object {
        private const val RESET_INPUT_TEXT = ""
        private const val ITEM_SPACING_VALUE = 8

        fun newInstance(bottariId: Long): TeamAssignedItemEditFragment =
            TeamAssignedItemEditFragment().apply {
                arguments = bundleOf(TeamAssignedItemEditViewModel.KEY_BOTTARI_ID to bottariId)
            }
    }
}
