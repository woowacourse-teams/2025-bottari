package com.bottari.presentation.view.edit.alarm

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.bottari.presentation.R
import com.bottari.presentation.databinding.DialogCalendarBinding
import com.bottari.presentation.view.edit.alarm.listener.OnDateClickListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

class CalendarDialog : DialogFragment() {
    private var _binding: DialogCalendarBinding? = null
    val binding: DialogCalendarBinding get() = _binding!!

    private val selectedDayDecorator: SelectedDayDecorator by lazy {
        SelectedDayDecorator(requireContext())
    }
    private val saturdayDecorator: SaturdayDecorator by lazy { SaturdayDecorator(requireContext()) }
    private val sundayDecorator: SundayDecorator by lazy { SundayDecorator(requireContext()) }
    private val pastDayDecorator: PastDayDecorator by lazy { PastDayDecorator() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        setupDialog()
        setupCalendar()
    }

    private fun setupListener() {
        binding.mcvAlarmDate.setOnMonthChangedListener { view, date ->
            view.removeDecorators()
            view.invalidateDecorators()
            view.addDecorators(
                saturdayDecorator,
                sundayDecorator,
                selectedDayDecorator,
                pastDayDecorator,
            )
        }
        binding.btnConfirm.setOnClickListener {
            val selectedDate =
                binding.mcvAlarmDate.selectedDate
                    ?.date
                    ?.toJavaLocalDate()
                    ?: return@setOnClickListener
            (parentFragment as? OnDateClickListener)?.onClick(selectedDate)
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun setupDialog() {
        val metrics = Resources.getSystem().displayMetrics
        val width = (metrics.widthPixels * WIDTH_RATIO).toInt()
        dialog?.run {
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            window?.setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    private fun setupCalendar() {
        binding.mcvAlarmDate.apply {
            setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
            setTitleFormatter(::formatCalendarTitle)
            setHeaderTextAppearance(R.style.CustomCalendarHeader)
            addDecorators(
                saturdayDecorator,
                sundayDecorator,
                selectedDayDecorator,
                pastDayDecorator,
            )
        }
    }

    private fun LocalDate.toJavaLocalDate(): java.time.LocalDate = java.time.LocalDate.of(year, monthValue, dayOfMonth)

    private fun formatCalendarTitle(day: CalendarDay): String {
        val date = day.date
        return requireContext().getString(
            R.string.calendar_title_format_text,
            date.year,
            date.monthValue,
        )
    }

    private class SelectedDayDecorator(
        context: Context,
    ) : DayViewDecorator {
        private val drawable = AppCompatResources.getDrawable(context, R.drawable.bg_date_selector)

        override fun decorate(view: DayViewFacade) {
            drawable?.let { drawable -> view.setSelectionDrawable(drawable) }
        }

        override fun shouldDecorate(day: CalendarDay?): Boolean = true
    }

    private class SaturdayDecorator(
        context: Context,
    ) : DayViewDecorator {
        private val span = TextAppearanceSpan(context, R.style.CustomCalendarSaturdayText)

        override fun decorate(view: DayViewFacade) {
            view.addSpan(span)
        }

        override fun shouldDecorate(day: CalendarDay): Boolean = day.date.dayOfWeek == DayOfWeek.SATURDAY
    }

    private class SundayDecorator(
        context: Context,
    ) : DayViewDecorator {
        private val span = TextAppearanceSpan(context, R.style.CustomCalendarSundayText)

        override fun decorate(view: DayViewFacade) {
            view.addSpan(span)
        }

        override fun shouldDecorate(day: CalendarDay): Boolean = day.date.dayOfWeek == DayOfWeek.SUNDAY
    }

    private class PastDayDecorator : DayViewDecorator {
        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true)
            view.addSpan(ForegroundColorSpan(Color.LTGRAY))
        }

        override fun shouldDecorate(day: CalendarDay): Boolean = day.date.isBefore(LocalDate.now())
    }

    companion object {
        private const val WIDTH_RATIO = 0.8
    }
}
