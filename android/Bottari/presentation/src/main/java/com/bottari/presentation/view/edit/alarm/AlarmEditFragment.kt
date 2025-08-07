package com.bottari.presentation.view.edit.alarm

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.common.extension.safeArgument
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentAlarmEditBinding
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.view.common.decoration.ItemSpacingDecoration
import com.bottari.presentation.view.edit.alarm.adapter.RepeatDayAdapter
import com.bottari.presentation.view.edit.alarm.listener.OnDateClickListener
import com.shawnlin.numberpicker.NumberPicker
import java.time.LocalDate
import java.time.LocalTime

class AlarmEditFragment :
    BaseFragment<FragmentAlarmEditBinding>(FragmentAlarmEditBinding::inflate),
    OnDateClickListener {
    private val viewModel: AlarmEditViewModel by viewModels {
        AlarmEditViewModel.Factory(
            ssaid = requireContext().getSSAID(),
            bottariId = safeArgument { getLong(ARG_BOTTARI_ID) },
            bottariTitle = safeArgument { getString(ARG_BOTTARI_TITLE) },
            alarm = safeArgument { getParcelableCompat(ARG_ALARM) },
        )
    }
    private val adapter: RepeatDayAdapter by lazy { RepeatDayAdapter(viewModel::updateDaysOfWeek) }
    private val scheduler: AlarmScheduler by lazy { AlarmScheduler() }
    private val hourPickers: List<NumberPicker> by lazy {
        listOf(
            binding.layoutNonRepeatAlarmTime.npAlarmTimeHour,
            binding.layoutRepeatAlarmTime.npAlarmTimeHour,
        )
    }
    private val minutePickers: List<NumberPicker> by lazy {
        listOf(
            binding.layoutNonRepeatAlarmTime.npAlarmTimeMinute,
            binding.layoutRepeatAlarmTime.npAlarmTimeMinute,
        )
    }
    private val groups: List<Group> by lazy {
        listOf(
            binding.groupAlarmNonRepeat,
            binding.groupAlarmRepeat,
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

    override fun onClick(date: LocalDate) {
        viewModel.updateAlarmDate(date)
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleAlarmState(uiState.alarm)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner, ::handleAlarmEvent)
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
        binding.btnCalendar.setOnClickListener {
            CalendarDialog().show(childFragmentManager, CalendarDialog::class.simpleName)
        }
        setupAlarmTimePickers()
        setupAlarmTypeSwitchers()
    }

    private fun handleAlarmState(alarm: AlarmUiModel) {
        updateAlarmTimePickers(alarm.time)
        adapter.submitList(alarm.repeatDays)
        binding.tvNonRepeatAlarmDate.text = alarm.date.toString()
    }

    private fun handleAlarmEvent(uiEvent: AlarmUiEvent) {
        when (uiEvent) {
            is AlarmUiEvent.AlarmCreateSuccess -> {
                scheduler.scheduleAlarm(uiEvent.notification)
                requireView().showSnackbar(R.string.alarm_edit_create_success_text)
                parentFragmentManager.popBackStack()
            }

            is AlarmUiEvent.AlarmSaveSuccess -> {
                scheduler.scheduleAlarm(uiEvent.notification)
                requireView().showSnackbar(R.string.alarm_edit_save_success_text)
                parentFragmentManager.popBackStack()
            }

            AlarmUiEvent.AlarmCreateFailure -> requireView().showSnackbar(R.string.alarm_edit_create_failure_text)
            AlarmUiEvent.AlarmSaveFailure -> requireView().showSnackbar(R.string.alarm_edit_save_failure_text)
        }
    }

    private fun updateAlarmTimePickers(alarmTime: LocalTime) {
        val hour = alarmTime.hour
        val minute = alarmTime.minute
        hourPickers.forEach { picker -> picker.value = hour }
        minutePickers.forEach { picker -> picker.value = minute }
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

    private fun setupAlarmTypeSwitchers() {
        binding.tvAlarmTypeNonRepeat.setOnClickListener {
            showOnly(binding.groupAlarmNonRepeat)
            viewModel.updateAlarmType(AlarmTypeUiModel.NON_REPEAT)
        }
        binding.tvAlarmTypeRepeat.setOnClickListener {
            showOnly(binding.groupAlarmRepeat)
            viewModel.updateAlarmType(AlarmTypeUiModel.REPEAT)
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

//    private fun handleAlarmDateChange(
//        month: Int,
//        day: Int,
//    ) {
//        val maxDay = getDayOfMonth(month)
//        val safeDay = minOf(day, maxDay)
//        if (binding.npNoRepeatAlarmDateDay.maxValue != maxDay) {
//            binding.npNoRepeatAlarmDateDay.maxValue = maxDay
//        }
//        if (binding.npNoRepeatAlarmDateDay.value != safeDay) {
//            binding.npNoRepeatAlarmDateDay.value = safeDay
//        }
//        val updatedDate = LocalDate.of(Year.now().value, month, safeDay)
//        viewModel.updateAlarmDate(updatedDate)
//    }
//
//    private fun getDayOfMonth(month: Int): Int {
//        val yearMonth = YearMonth.of(Year.now().value, month)
//        return yearMonth.lengthOfMonth()
//    }

    private fun showOnly(visibleView: View) {
        groups.forEach { it.isVisible = it == visibleView }
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val ARG_BOTTARI_TITLE = "ARG_BOTTARI_TITLE"
        private const val ARG_ALARM = "ARG_ALARM"

        fun newBundle(
            bottariId: Long,
            bottariTitle: String,
            alarm: AlarmUiModel?,
        ) = Bundle().apply {
            putLong(ARG_BOTTARI_ID, bottariId)
            putString(ARG_BOTTARI_TITLE, bottariTitle)
            putParcelable(ARG_ALARM, alarm)
        }
    }
}
