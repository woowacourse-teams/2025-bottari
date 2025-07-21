package com.bottari.presentation.view.edit.personal.main

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
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditAlarmAdapter
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class PersonalBottariEditFragment :
    BaseFragment<FragmentMainEditBinding>(FragmentMainEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        PersonalBottariEditViewModel.Factory(getBottariId())
    }
    private val itemAdapter: PersonalBottariEditItemAdapter by lazy { PersonalBottariEditItemAdapter() }
    private val alarmAdapter: PersonalBottariEditAlarmAdapter by lazy { PersonalBottariEditAlarmAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    private fun setupObserver() {
        viewModel.bottari.observe(viewLifecycleOwner, ::handleBottariState)
        viewModel.items.observe(viewLifecycleOwner, ::handleItemsState)
        viewModel.alarms.observe(viewLifecycleOwner, ::handleAlarmState)
    }

    private fun setupUI() {
        setupItemRecyclerView()
        setupAlarmRecyclerView()
        setupToolbar()
    }

    private fun getBottariId(): Long = arguments?.getLong(EXTRA_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun handleBottariState(uiState: UiState<BottariUiModel>) {
        when (uiState) {
            is UiState.Loading -> showSnackbar(R.string.home_nav_market_title)
            is UiState.Success -> {
                binding.tvBottariTitle.text = uiState.data.title
            }

            is UiState.Failure -> showSnackbar(R.string.home_nav_profile_title)
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
            is UiState.Loading -> Unit
            is UiState.Success -> {
                alarmAdapter.submitList(uiState.data)
                toggleAlarmSelection(uiState.data.isNotEmpty())
            }

            is UiState.Failure -> Unit
        }
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
        binding.swAlarm.isVisible = hasAlarms
    }

    private fun setupItemRecyclerView() {
        binding.rvEditItem.apply {
            layoutManager =
                FlexboxLayoutManager(requireContext()).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.FLEX_START
                }
            adapter = this@PersonalBottariEditFragment.itemAdapter
        }
    }

    private fun setupAlarmRecyclerView() {
        binding.rvEditAlarm.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PersonalBottariEditFragment.alarmAdapter
        }
    }

    private fun setupToolbar() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
        private const val INVALID_BOTTARI_ID = -1L

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
