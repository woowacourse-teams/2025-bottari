package com.bottari.alarm;

import static org.assertj.core.api.Assertions.assertThat;

import com.bottari.alarm.domain.RepeatType;
import com.bottari.alarm.domain.RoutineAlarm;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RoutineAlarmTest {

    @DisplayName("요일에 따라 비트마스크를 계산한다.")
    @Test
    void encodeDayOfWeeks() {
        // given
        final Set<DayOfWeek> dayOfWeeks = EnumSet.of(
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
        );
        final int expected = 0b1010010;

        // when
        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.MAX,
                RepeatType.EVERY_WEEK_REPEAT,
                LocalDate.MAX,
                dayOfWeeks
        );

        // then
        assertThat(routineAlarm.getRepeatDayOfWeeksBitmask()).isEqualTo(expected);
    }

    @DisplayName("반복하는 요일을 가져온다.")
    @Test
    void repeatDayOfWeeks() {
        // given
        final Set<DayOfWeek> dayOfWeeks = EnumSet.of(
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
        );
        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.MAX,
                RepeatType.EVERY_WEEK_REPEAT,
                LocalDate.MAX,
                dayOfWeeks
        );

        // when
        final Set<DayOfWeek> actual = routineAlarm.repeatDayOfWeeks();

        // then
        assertThat(actual).containsExactlyInAnyOrder(
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
        );
    }

    @DisplayName("반복 유형에 따라 요일 비트마스크를 결정한다.")
    @ParameterizedTest
    @MethodSource("provideTypeAndBitmask")
    void resolveBitmask(
            final RepeatType type,
            final int expected
    ) {
        // given
        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.MAX,
                type,
                null,
                null
        );

        // when
        final int actual = routineAlarm.getRepeatDayOfWeeksBitmask();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideTypeAndBitmask() {
        return Stream.of(
                Arguments.of(RepeatType.NON_REPEAT, 0),
                Arguments.of(RepeatType.EVERY_DAY_REPEAT, 0b1111111)
        );
    }
}
