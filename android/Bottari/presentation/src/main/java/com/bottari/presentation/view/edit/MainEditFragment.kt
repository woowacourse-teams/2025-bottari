package com.bottari.presentation.view.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.databinding.FragmentMainEditBinding
import com.bottari.presentation.view.edit.adapter.PersonalBottariAlarmAdapter
import com.bottari.presentation.view.edit.adapter.PersonalBottariItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MainEditFragment : BaseFragment<FragmentMainEditBinding>(FragmentMainEditBinding::inflate) {
    private val viewModel: MainEditViewModel by viewModels()
    private val itemAdapter = PersonalBottariItemAdapter()
    private val alarmAdapter = PersonalBottariAlarmAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupItemRecyclerView()
        setupAlarmRecyclerView()
        fetchInitialData()
    }

    private fun setupObservers() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            itemAdapter.submitList(items)
            toggleItemSection(items.isNotEmpty())
        }

        viewModel.alarms.observe(viewLifecycleOwner) { alarms ->
            alarmAdapter.submitList(alarms)
            toggleAlarmSelection(alarms.isNotEmpty())
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
    }

    private fun setupItemRecyclerView() {
        binding.rvEditItem.apply {
            layoutManager =
                FlexboxLayoutManager(requireContext()).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.FLEX_START
                }
            adapter = this@MainEditFragment.itemAdapter
        }
    }

    private fun setupAlarmRecyclerView() {
        binding.rvEditAlarm.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainEditFragment.alarmAdapter
        }
    }

    private fun fetchInitialData() {
        viewModel.fetchItemsById(1)
        viewModel.fetchAlarmById(1)
    }
}
