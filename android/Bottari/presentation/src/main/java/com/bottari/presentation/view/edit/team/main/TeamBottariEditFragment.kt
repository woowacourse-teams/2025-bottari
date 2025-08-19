package com.bottari.presentation.view.edit.team.main

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentTeamBottariEditBinding
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.view.edit.team.TeamBottariEditNavigator
import com.bottari.presentation.view.edit.team.main.adapter.TeamBottariEditItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TeamBottariEditFragment : BaseFragment<FragmentTeamBottariEditBinding>(FragmentTeamBottariEditBinding::inflate) {
    private val teamBottariId: Long by lazy { requireArguments().getLong(ARG_BOTTARI_ID) }
    private val viewModel: TeamBottariEditViewModel by viewModels {
        TeamBottariEditViewModel.Factory(teamBottariId)
    }
    private val personalItemAdapter: TeamBottariEditItemAdapter by lazy {
        TeamBottariEditItemAdapter(BottariItemTypeUiModel.PERSONAL)
    }
    private val assignedItemAdapter: TeamBottariEditItemAdapter by lazy {
        TeamBottariEditItemAdapter(BottariItemTypeUiModel.ASSIGNED())
    }
    private val sharedItemAdapter: TeamBottariEditItemAdapter by lazy {
        TeamBottariEditItemAdapter(BottariItemTypeUiModel.SHARED)
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
        viewModel.fetchTeamBottariDetail()
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, ::handleUiState)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleUiEvent)
    }

    private fun setupUI() {
        setupItemTitles()
        binding.viewTeamPersonalItemEdit.rvItemEdit.adapter = personalItemAdapter
        binding.viewTeamPersonalItemEdit.rvItemEdit.layoutManager = createFlexBoxLayoutManager()
        binding.viewTeamAssignedItemEdit.rvItemEdit.adapter = assignedItemAdapter
        binding.viewTeamAssignedItemEdit.rvItemEdit.layoutManager = createFlexBoxLayoutManager()
        binding.viewTeamSharedItemEdit.rvItemEdit.adapter = sharedItemAdapter
        binding.viewTeamSharedItemEdit.rvItemEdit.layoutManager = createFlexBoxLayoutManager()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener { handlePreviousButtonClick() }
        binding.viewTeamMemberEdit.root.setOnClickListener {
            (requireActivity() as? TeamBottariEditNavigator)?.navigateToMemberEdit(teamBottariId)
        }
        binding.viewTeamPersonalItemEdit.btnRoot.setOnClickListener {
            navigateToItemEdit(
                BottariItemTypeUiModel.PERSONAL,
            )
        }
        binding.viewTeamSharedItemEdit.btnRoot.setOnClickListener {
            navigateToItemEdit(
                BottariItemTypeUiModel.SHARED,
            )
        }
        binding.viewTeamAssignedItemEdit.btnRoot.setOnClickListener {
            navigateToItemEdit(
                BottariItemTypeUiModel.ASSIGNED(),
            )
        }
    }

    private fun navigateToItemEdit(type: BottariItemTypeUiModel) {
        (requireActivity() as? TeamBottariEditNavigator)?.navigateToItemEdit(teamBottariId, type)
    }

    private fun handleUiState(uiState: TeamBottariEditUiState) {
        toggleLoadingIndicator(uiState.isLoading)

        binding.viewTeamAlarmEdit.switchAlarmEdit.isChecked = uiState.alarmSwitchState
        binding.viewTeamAlarmEdit.viewAlarmEditEmpty.root.isVisible = uiState.alarmSwitchState

        binding.tvTeamEditTitle.text = uiState.bottariTitle
        handlePersonalItemEmptyViews(uiState.isPersonalItemsEmpty)
        handleAssignedItemEmptyViews(uiState.isAssignedItemsEmpty)
        handleSharedItemEmptyViews(uiState.isSharedItemsEmpty)
        handleAlarmState(uiState.alarm)
        handleAlarmEmptyViews(uiState.isAlarmNull)
        personalItemAdapter.submitList(uiState.personalItems)
        assignedItemAdapter.submitList(uiState.assignedItems)
        sharedItemAdapter.submitList(uiState.sharedItems)
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

    private fun handleAlarmEmptyViews(isAlarmNull: Boolean) {
        binding.viewTeamAlarmEdit.tvAlarmEditDescription.isVisible = !isAlarmNull
        binding.viewTeamAlarmEdit.groupAlarmItem.isVisible = !isAlarmNull
    }

    private fun handleAlarmState(alarm: AlarmUiModel?) {
        if (alarm == null) return
        val timeFormat = getString(R.string.common_format_time_alarm)
        binding.viewTeamAlarmEdit.tvAlarmTime.text = alarm.time.formatWithPattern(timeFormat)
        binding.viewTeamAlarmEdit.tvAlarmType.text = createAlarmTypeText(alarm)
    }

    private fun createAlarmTypeText(alarm: AlarmUiModel): String {
        return when (alarm.type) {
            AlarmTypeUiModel.NON_REPEAT -> alarm.date.formatWithPattern(getString(R.string.common_format_date_alarm))
            AlarmTypeUiModel.REPEAT -> {
                if (alarm.isRepeatEveryDay) {
                    return getString(R.string.bottari_item_alarm_repeat_everyday_text)
                }
                return getString(R.string.bottari_item_alarm_repeat_everyweek_text) +
                    getString(R.string.common_separator_text) +
                    alarm.repeatDays.joinToString()
            }
        }
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

    private fun createFlexBoxLayoutManager(): FlexboxLayoutManager =
        FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

    private fun handlePreviousButtonClick() {
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
