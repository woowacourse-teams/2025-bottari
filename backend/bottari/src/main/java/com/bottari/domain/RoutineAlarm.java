package com.bottari.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    월 0000001
    화 0000010
    수 0000100
    목 0001000
    금 0010000
    토 0100000
    일 1000000
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoutineAlarm {

    private static final int EMPTY_DAY_OF_WEEKS_BITMASK = 0;
    private static final int EVERY_DAY_OF_WEEKS_BITMASK = 0b1111111;

    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private RepeatType type;

    private LocalDate date;

    private int repeatDayOfWeeksBitmask;

    public RoutineAlarm(
            final LocalTime time,
            final RepeatType type,
            final LocalDate date,
            final Set<DayOfWeek> dayOfWeeks
    ) {
        this.time = time;
        this.type = type;
        this.date = date;
        this.repeatDayOfWeeksBitmask = resolveBitmask(dayOfWeeks, type);
    }

    public Set<DayOfWeek> repeatDayOfWeeks() {
        return Stream.of(DayOfWeek.values())
                .filter(this::isActiveOn)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(DayOfWeek.class)));
    }

    private int resolveBitmask(
            final Set<DayOfWeek> dayOfWeeks,
            final RepeatType type
    ) {
        return switch (type) {
            case NON_REPEAT -> EMPTY_DAY_OF_WEEKS_BITMASK;
            case EVERY_DAY_REPEAT -> EVERY_DAY_OF_WEEKS_BITMASK;
            case EVERY_WEEK_REPEAT -> encodeDayOfWeeks(dayOfWeeks);
        };
    }

    private int encodeDayOfWeeks(final Set<DayOfWeek> dayOfWeeks) {
        int value = EMPTY_DAY_OF_WEEKS_BITMASK;
        for (final DayOfWeek dayOfWeek : dayOfWeeks) {
            value |= bitValue(dayOfWeek);
        }

        return value;
    }

    private boolean isActiveOn(final DayOfWeek dayOfWeek) {
        return (repeatDayOfWeeksBitmask & bitValue(dayOfWeek)) != EMPTY_DAY_OF_WEEKS_BITMASK;
    }

    private int bitValue(final DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> 1;
            case TUESDAY -> 1 << 1;
            case WEDNESDAY -> 1 << 2;
            case THURSDAY -> 1 << 3;
            case FRIDAY -> 1 << 4;
            case SATURDAY -> 1 << 5;
            case SUNDAY -> 1 << 6;
        };
    }
}
