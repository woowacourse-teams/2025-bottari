package com.bottari.presentation.view.edit.team.main

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamBottariEditBinding
import com.bottari.presentation.view.edit.team.TeamBottariEditNavigator

class TeamBottariEditFragment : BaseFragment<FragmentTeamBottariEditBinding>(FragmentTeamBottariEditBinding::inflate) {
    private val viewModel: TeamBottariEditViewModel by viewModels {
        TeamBottariEditViewModel.Factory(
            bottariId = requireArguments().getLong(ARG_BOTTARI_ID),
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

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun setupUI() {
        setupItemTitles()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener(::handlePreviousButtonClick)
    }

    private fun handleUiState(uiState: TeamBottariEditUiState) {
        toggleLoadingIndicator(uiState.isLoading)
        handlePersonalItemEmptyViews(uiState.isPersonalItemsEmpty)
        handleAssignedItemEmptyViews(uiState.isAssignedItemsEmpty)
        handleSharedItemEmptyViews(uiState.isSharedItemsEmpty)
    }

    private fun handlePersonalItemEmptyViews(isItemEmpty: Boolean) {
        binding.viewTeamPersonalItemEdit.viewItemEditEmpty.root.isVisible = isItemEmpty
        binding.viewTeamPersonalItemEdit.tvItemEditDescription.isVisible = !isItemEmpty
    }

    private fun handleAssignedItemEmptyViews(isItemEmpty: Boolean) {
        binding.viewTeamAssignedItemEdit.viewItemEditEmpty.root.isVisible = isItemEmpty
        binding.viewTeamAssignedItemEdit.tvItemEditDescription.isVisible = !isItemEmpty
    }

    private fun handleSharedItemEmptyViews(isItemEmpty: Boolean) {
        binding.viewTeamSharedItemEdit.viewItemEditEmpty.root.isVisible = isItemEmpty
        binding.viewTeamSharedItemEdit.tvItemEditDescription.isVisible = !isItemEmpty
    }

    private fun handleUiEvent(uiEvent: TeamBottariEditUiEvent) {
        when (uiEvent) {
            TeamBottariEditUiEvent.FetchTeamBottariDetailFailure ->
                showSnackbar(R.string.bottari_edit_fetch_failure_text)

            TeamBottariEditUiEvent.ToggleAlarmStateFailure ->
                showSnackbar(R.string.bottari_edit_toggle_alarm_state_failure_text)
        }
    }

    private fun showSnackbar(
        @StringRes messageRes: Int,
    ) = requireView().showSnackbar(messageRes)

    private fun setupItemTitles() {
        binding.viewTeamPersonalItemEdit.tvItemEditTitle.text =
            getString(R.string.bottari_edit_personal_items_title_text)
        binding.viewTeamAssignedItemEdit.tvItemEditTitle.text =
            getString(R.string.bottari_edit_assigned_items_title_text)
        binding.viewTeamSharedItemEdit.tvItemEditTitle.text =
            getString(R.string.bottari_edit_shared_items_title_text)
    }

    private fun handlePreviousButtonClick(view: View) {
        (requireActivity() as? TeamBottariEditNavigator)?.navigateBack()
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamBottariEditFragment =
            TeamBottariEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
