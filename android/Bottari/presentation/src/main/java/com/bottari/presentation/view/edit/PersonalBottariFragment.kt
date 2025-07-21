package com.bottari.presentation.view.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentMainEditBinding
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.checklist.ChecklistViewModel
import com.bottari.presentation.view.edit.adapter.PersonalBottariAlarmAdapter
import com.bottari.presentation.view.edit.adapter.PersonalBottariItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class PersonalBottariFragment :
    BaseFragment<FragmentMainEditBinding>(FragmentMainEditBinding::inflate) {
    private val viewModel: PersonalBottariViewModel by viewModels {
        PersonalBottariViewModel.Factory(getBottariId())
    }
    private val itemAdapter = PersonalBottariItemAdapter()
    private val alarmAdapter = PersonalBottariAlarmAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun getBottariId(): Long = arguments?.getLong(EXTRAS_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun setupObserver() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            handleItemsState(items)
        }
        viewModel.alarms.observe(viewLifecycleOwner) { alarms ->
            handleAlarmState(alarms)
        }
    }

    private fun handleItemsState(uiState: UiState<List<ItemUiModel>>) {
        when (uiState) {
            is UiState.Loading -> showSnackbar(R.string.home_nav_market_title)
            is UiState.Success -> {
                itemAdapter.submitList(uiState.data)
                toggleItemSection(uiState.data.isNotEmpty())
            }

            is UiState.Failure -> showSnackbar(R.string.home_nav_profile_title)
        }
    }

    private fun handleAlarmState(uiState: UiState<List<AlarmTypeUiModel>>) {
        when (uiState) {
            is UiState.Loading -> showSnackbar(R.string.home_nav_market_title)
            is UiState.Success -> {
                alarmAdapter.submitList(uiState.data)
                toggleAlarmSelection(uiState.data.isNotEmpty())
            }

            is UiState.Failure -> showSnackbar(R.string.home_nav_profile_title)
        }
    }

    private fun setupUI() {
        setupItemRecyclerView()
        setupAlarmRecyclerView()
    }

    private fun toggleItemSection(hasItems: Boolean) {
        binding.tvClickEditItemTitle.isVisible = !hasItems
        binding.tvClickEditItemDescription.isVisible = !hasItems
        binding.viewClickEditItem.isVisible = !hasItems
        binding.tvClickEditItemDescriptionNotEmpty.isVisible = hasItems
        binding.rvEditItem.isVisible = hasItems
    }

    private fun toggleAlarmSelection(hasAlarms: Boolean) {
        binding.tvClickEditAlarmTitle.isVisible = !hasAlarms
        binding.tvClickEditAlarmDescription.isVisible = !hasAlarms
        binding.viewClickEditAlarm.isVisible = !hasAlarms
        binding.tvClickEditAlarmDescriptionNotEmpty.isVisible = hasAlarms
        binding.rvEditAlarm.isVisible = hasAlarms
    }

    private fun setupItemRecyclerView() {
        binding.rvEditItem.apply {
            layoutManager =
                FlexboxLayoutManager(requireContext()).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.FLEX_START
                }
            adapter = this@PersonalBottariFragment.itemAdapter
        }
    }

    private fun setupAlarmRecyclerView() {
        binding.rvEditAlarm.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PersonalBottariFragment.alarmAdapter
        }
    }

    companion object{
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
        private const val INVALID_BOTTARI_ID = -1L
    }
}
