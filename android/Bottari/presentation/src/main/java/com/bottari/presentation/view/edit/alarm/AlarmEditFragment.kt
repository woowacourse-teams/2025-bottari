package com.bottari.presentation.view.edit.alarm

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.common.ItemSpacingDecoration
import com.bottari.presentation.databinding.FragmentAlarmEditBinding
import com.bottari.presentation.extension.getParcelableCompat
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.view.edit.alarm.adapter.DayOfWeekAdapter
import com.google.android.material.snackbar.Snackbar
import com.shawnlin.numberpicker.NumberPicker
import java.time.LocalDate
import java.time.LocalTime
import java.time.Year
import java.time.YearMonth

class AlarmEditFragment : BaseFragment<FragmentAlarmEditBinding>(FragmentAlarmEditBinding::inflate) {
    private val viewModel: AlarmEditViewModel by viewModels {
        AlarmEditViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            bottariId = safeArgument { getLong(EXTRA_BOTTARI_ID) },
            alarm = safeArgument { getParcelableCompat(EXTRA_ALARM) },
        )
    }
    private val adapter: DayOfWeekAdapter by lazy { DayOfWeekAdapter(viewModel::selectDayOfWeek) }
    private val hourPickers: List<NumberPicker> by lazy {
        listOf(
            binding.layoutNoRepeatAlarmTime.npAlarmTimeHour,
            binding.layoutEverydayRepeatAlarmTime.npAlarmTimeHour,
            binding.layoutEveryweekRepeatAlarmTime.npAlarmTimeHour,
        )
    }
    private val minutePickers: List<NumberPicker> by lazy {
        listOf(
            binding.layoutNoRepeatAlarmTime.npAlarmTimeMinute,
            binding.layoutEverydayRepeatAlarmTime.npAlarmTimeMinute,
            binding.layoutEveryweekRepeatAlarmTime.npAlarmTimeMinute,
        )
    }
    private val groups: List<Group> by lazy {
        listOf(
            binding.groupAlarmNoRepeat,
            binding.groupAlarmEverydayRepeat,
            binding.groupAlarmEveryweekRepeat,
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

    private inline fun <T> Fragment.safeArgument(block: Bundle.() -> T): T? = runCatching { requireArguments().block() }.getOrNull()

    private fun setupObserver() {
        viewModel.alarm.observe(viewLifecycleOwner) { alarm ->
            updateAlarmTimePickers(alarm.time)
            updateAlarmDatePickers(alarm.date)
            adapter.submitList(alarm.daysOfWeek)
        }
        viewModel.saveState.observe(viewLifecycleOwner, ::handleSaveState)
    }

    private fun setupUI() {
        binding.rvDayOfWeek.apply {
            adapter = this@AlarmEditFragment.adapter
            layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            addItemDecoration(
                ItemSpacingDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.space_x_small,
                    ),
                ),
            )
        }
    }

    private fun setupListener() {
        binding.btnClose.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnConfirm.setOnClickListener { viewModel.updateAlarm() }
        setupAlarmTimePickers()
        setupAlarmDatePickers()
        setupAlarmTypeSwitchers()
    }

    private fun updateAlarmTimePickers(alarmTime: LocalTime) {
        val hour = alarmTime.hour
        val minute = alarmTime.minute
        hourPickers.forEach { picker -> picker.value = hour }
        minutePickers.forEach { picker -> picker.value = minute }
    }

    private fun updateAlarmDatePickers(alarmDate: LocalDate) {
        binding.npNoRepeatAlarmDateMonth.value = alarmDate.monthValue
        binding.npNoRepeatAlarmDateDay.value = alarmDate.dayOfMonth
    }

    private fun setupAlarmTimePickers() {
        hourPickers
            .zip(minutePickers)
            .forEach { (hourPicker, minutePicker) ->
                bindAlarmTimePickers(
                    hourPicker,
                    minutePicker,
                )
            }
    }

    private fun handleSaveState(uiState: UiState<Unit>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                showSnackbar(R.string.alarm_edit_save_success_text, Snackbar.LENGTH_SHORT)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            is UiState.Failure -> Unit
        }
    }

    private fun setupAlarmDatePickers() {
        binding.npNoRepeatAlarmDateMonth.setOnValueChangedListener { _, _, newVal ->
            handleAlarmDateChange(
                month = newVal,
                day = binding.npNoRepeatAlarmDateDay.value,
            )
        }
        binding.npNoRepeatAlarmDateDay.setOnValueChangedListener { _, _, newVal ->
            handleAlarmDateChange(
                month = binding.npNoRepeatAlarmDateMonth.value,
                day = newVal,
            )
        }
    }

    private fun setupAlarmTypeSwitchers() {
        binding.tvAlarmTypeNoRepeat.setOnClickListener {
            showOnly(binding.groupAlarmNoRepeat)
            viewModel.updateAlarmType(AlarmTypeUiModel.NON_REPEAT)
        }
        binding.tvAlarmTypeEverydayRepeat.setOnClickListener {
            showOnly(binding.groupAlarmEverydayRepeat)
            viewModel.updateAlarmType(AlarmTypeUiModel.EVERYDAY_REPEAT)
        }
        binding.tvAlarmTypeEveryweekRepeat.setOnClickListener {
            showOnly(binding.groupAlarmEveryweekRepeat)
            viewModel.updateAlarmType(AlarmTypeUiModel.EVERYWEEK_REPEAT)
        }
    }

    private fun bindAlarmTimePickers(
        hourPicker: NumberPicker,
        minutePicker: NumberPicker,
    ) {
        hourPicker.setOnValueChangedListener { _, _, newVal ->
            handleAlarmTimeChange(newVal, minutePicker.value)
        }
        minutePicker.setOnValueChangedListener { _, _, newVal ->
            handleAlarmTimeChange(hourPicker.value, newVal)
        }
    }

    private fun handleAlarmTimeChange(
        hour: Int,
        minute: Int,
    ) {
        val updatedTime = LocalTime.of(hour, minute)
        viewModel.updateAlarmTime(time = updatedTime)
    }

    private fun handleAlarmDateChange(
        month: Int,
        day: Int,
    ) {
        val maxDay = getDayOfMonth(month)
        val safeDay = minOf(day, maxDay)
        binding.npNoRepeatAlarmDateDay.maxValue = getDayOfMonth(month)
        binding.npNoRepeatAlarmDateDay.value = safeDay
        val updatedDate = LocalDate.of(Year.now().value, month, day)
        viewModel.updateAlarmDate(date = updatedDate)
    }

    private fun getDayOfMonth(month: Int): Int {
        val yearMonth = YearMonth.of(Year.now().value, month)
        return yearMonth.lengthOfMonth()
    }

    private fun showOnly(visibleView: View) {
        groups.forEach { it.isVisible = it == visibleView }
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_ALARM = "EXTRA_ALARM"

        fun newBundle(
            bottariId: Long,
            alarm: AlarmUiModel?,
        ) = Bundle().apply {
            putLong(EXTRA_BOTTARI_ID, bottariId)
            putParcelable(EXTRA_ALARM, alarm)
        }
    }
}
