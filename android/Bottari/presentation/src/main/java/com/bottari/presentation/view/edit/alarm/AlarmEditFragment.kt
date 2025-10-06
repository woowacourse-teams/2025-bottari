package com.bottari.presentation.view.edit.alarm

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.safeArgument
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentAlarmEditBinding
import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.util.AlarmScheduler.scheduleAlarm
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
            bottariId = safeArgument { getLong(ARG_BOTTARI_ID) },
            bottariTitle = safeArgument { getString(ARG_BOTTARI_TITLE) },
        )
    }
    private val adapter: RepeatDayAdapter by lazy { RepeatDayAdapter(viewModel::updateDaysOfWeek) }
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
        collectWithLifecycle(viewModel.uiState) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            handleConfirmButtonState(uiState.isRepeatWithoutDays.not())
            uiState.alarm?.let { alarm ->
                handleAlarmState(alarm)
                if (alarm.type == AlarmTypeUiModel.NON_REPEAT) {
                    showOnly(binding.groupAlarmNonRepeat)
                    return@collectWithLifecycle
                }
                showOnly(binding.groupAlarmRepeat)
            }
        }
        collectWithLifecycle(viewModel.uiEvent) { uiEvent -> handleAlarmEvent(uiEvent) }
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

    private fun handleConfirmButtonState(isEnabled: Boolean) {
        binding.btnConfirm.isEnabled = isEnabled
        val textColorRes = if (isEnabled) R.color.black else R.color.gray_700
        val textColor = ContextCompat.getColor(requireContext(), textColorRes)
        binding.btnConfirm.setTextColor(textColor)
    }

    private fun handleAlarmState(alarm: AlarmUiModel) {
        updateAlarmTimePickers(alarm.time)
        adapter.submitList(alarm.repeatDays)
        binding.tvNonRepeatAlarmDate.text = alarm.date.toString()
    }

    private fun handleAlarmEvent(uiEvent: AlarmUiEvent) {
        when (uiEvent) {
            is AlarmUiEvent.SaveAlarmSuccess -> {
                scheduleAlarm(notification = uiEvent.notification.toDomain())
                requireView().showSnackbar(R.string.alarm_edit_save_success_text)
                parentFragmentManager.popBackStack()
            }

            AlarmUiEvent.FetchAlarmFailure -> requireView().showSnackbar(R.string.alarm_edit_fetch_failure_text)
            AlarmUiEvent.SaveAlarmFailure -> requireView().showSnackbar(R.string.alarm_edit_save_failure_text)
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

    private fun showOnly(visibleView: View) {
        groups.forEach { group ->
            val isVisible = group == visibleView
            group.isVisible = isVisible
            when (group) {
                binding.groupAlarmNonRepeat ->
                    updateAlarmTypeText(
                        binding.tvAlarmTypeNonRepeat,
                        isVisible,
                    )

                binding.groupAlarmRepeat ->
                    updateAlarmTypeText(
                        binding.tvAlarmTypeRepeat,
                        isVisible,
                    )
            }
        }
    }

    private fun updateAlarmTypeText(
        textView: TextView,
        isSelected: Boolean,
    ) {
        val bgColorRes = if (isSelected) R.color.primary else R.color.white
        val bgColor = ContextCompat.getColor(requireContext(), bgColorRes)

        val textColorRes = if (isSelected) R.color.white else R.color.black
        val textColor = ContextCompat.getColor(requireContext(), textColorRes)

        textView.backgroundTintList = ColorStateList.valueOf(bgColor)
        textView.setTextColor(textColor)
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"
        private const val ARG_BOTTARI_TITLE = "ARG_BOTTARI_TITLE"

        fun newBundle(
            bottariId: Long,
            bottariTitle: String,
        ) = Bundle().apply {
            putLong(ARG_BOTTARI_ID, bottariId)
            putString(ARG_BOTTARI_TITLE, bottariTitle)
        }
    }
}
