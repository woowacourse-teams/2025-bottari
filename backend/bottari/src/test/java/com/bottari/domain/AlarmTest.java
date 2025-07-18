package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlarmTest {

    @DisplayName("루틴 알람 없이 위치 알람을 설정하는 경우, 예외를 던진다.")
    @Test
    void validateLocationAlarm() {
        // given
        final LocationAlarm locationAlarm = new LocationAlarm(false, 1.23, 1.23, 100);
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);

        // when & then
        assertThatThrownBy(() -> new Alarm(false, null, locationAlarm, bottari))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("루틴 알람이 존재하지 않으면 위치 알람을 설정할 수 없습니다.");
    }

    @DisplayName("알람을 수정할 때, 위치 알람 없이 루틴 알람을 설정하는 경우, 예외를 던진다.")
    @Test
    void update_Exception_LocationAlarm() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);
        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.NOON,
                RepeatType.NON_REPEAT,
                LocalDate.MAX,
                Set.of()
        );
        final LocationAlarm locationAlarm = new LocationAlarm(false, 1.23, 1.23, 100);
        final Alarm alarm = new Alarm(false, routineAlarm, locationAlarm, bottari);

        // when & then
        assertThatThrownBy(() -> alarm.update(null, locationAlarm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("루틴 알람이 존재하지 않으면 위치 알람을 설정할 수 없습니다.");
    }
}
