package com.bottari.fixture;

import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public enum RoutineAlarmFixture {

    NON_REPEAT_ALARM(
            LocalTime.of(10, 0),
            RepeatType.NON_REPEAT,
            LocalDate.now().plusDays(1),
            Set.of()
    ),
    EVERY_DAY_REPEAT_ALARM(
            LocalTime.of(10, 0),
            RepeatType.EVERY_DAY_REPEAT,
            null,
            Set.of()
    ),
    EVERY_WEEK_REPEAT_ALARM(
            LocalTime.of(10, 0),
            RepeatType.EVERY_WEEK_REPEAT,
            null,
            Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
    );

    private final LocalTime time;
    private final RepeatType type;
    private final LocalDate date;
    private final Set<DayOfWeek> dayOfWeeks;

    RoutineAlarmFixture(
            final LocalTime time,
            final RepeatType type,
            final LocalDate date,
            final Set<DayOfWeek> dayOfWeeks
    ) {
        this.time = time;
        this.type = type;
        this.date = date;
        this.dayOfWeeks = dayOfWeeks;
    }

    public RoutineAlarm getRoutineAlarm() {
        return new RoutineAlarm(time, type, date, dayOfWeeks);
    }
}
