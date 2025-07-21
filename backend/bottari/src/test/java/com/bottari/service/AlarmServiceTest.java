package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.Member;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import com.bottari.dto.CreateAlarmRequest;
import com.bottari.dto.UpdateAlarmRequest;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(AlarmService.class)
class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("알람을 추가한다.")
    @Test
    void create() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final CreateAlarmRequest request = new CreateAlarmRequest(
                new CreateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.MAX,
                        RepeatType.EVERY_WEEK_REPEAT,
                        null,
                        List.of(1, 4, 7) // 월, 목, 일
                ),
                new CreateAlarmRequest.LocationAlarmRequest(
                        true,
                        1.23,
                        1.23,
                        100
                )
        );

        // when
        final Long actual = alarmService.create(bottari.getId(), request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하지 않는 보따리에 알람을 추가할 경우, 예외를 던진다.")
    @Test
    void create_Exception_NotExistsBottari() {
        // given
        final CreateAlarmRequest request = new CreateAlarmRequest(
                new CreateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.MAX,
                        RepeatType.EVERY_WEEK_REPEAT,
                        null,
                        List.of(1, 4, 7) // 월, 목, 일
                ),
                new CreateAlarmRequest.LocationAlarmRequest(
                        true,
                        1.23,
                        1.23,
                        100
                )
        );
        final Long invalidBottariId = 1L;

        // when & then
        assertThatThrownBy(() -> alarmService.create(invalidBottariId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 보따리입니다.");
    }

    @DisplayName("알람을 수정한다.")
    @Test
    void update() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.now(),
                RepeatType.EVERY_WEEK_REPEAT,
                null,
                Set.of(DayOfWeek.MONDAY)
        );
        final LocationAlarm locationAlarm = new LocationAlarm(
                true,
                37.5,
                127.5,
                100
        );
        final Alarm alarm = new Alarm(true, routineAlarm, locationAlarm, bottari);
        entityManager.persist(alarm);

        final UpdateAlarmRequest updateRequest = new UpdateAlarmRequest(
                new UpdateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.NOON,
                        RepeatType.NON_REPEAT,
                        LocalDate.MAX,
                        List.of()
                ),
                new UpdateAlarmRequest.LocationAlarmRequest(
                        false,
                        1.23,
                        1.23,
                        100
                )
        );

        // when
        alarmService.update(alarm.getId(), updateRequest);

        // then
        final Alarm actual = entityManager.find(Alarm.class, alarm.getId());
        assertAll(() -> {
            assertThat(actual.getRoutineAlarm().getTime()).isEqualTo(LocalTime.NOON);
            assertThat(actual.getRoutineAlarm().getType()).isEqualTo(RepeatType.NON_REPEAT);
            assertThat(actual.getRoutineAlarm().getDate()).isEqualTo(LocalDate.MAX);
            assertThat(actual.getRoutineAlarm().getRepeatDayOfWeeksBitmask()).isEqualTo(0);
            assertThat(actual.getLocationAlarm().isLocationAlarmActive()).isFalse();
            assertThat(actual.getLocationAlarm().latitude()).isEqualTo(1.23);
            assertThat(actual.getLocationAlarm().longitude()).isEqualTo(1.23);
        });
    }

    @DisplayName("존재하지 않는 알람을 수정할 경우, 예외를 던진다.")
    @Test
    void update_Exception_NotExistsAlarm() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final UpdateAlarmRequest updateRequest = new UpdateAlarmRequest(
                new UpdateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.NOON,
                        RepeatType.NON_REPEAT,
                        LocalDate.MAX,
                        List.of()
                ),
                new UpdateAlarmRequest.LocationAlarmRequest(
                        false,
                        1.23,
                        1.23,
                        100
                )
        );
        final Long invalidAlarmId = 1L;

        // when & then
        assertThatThrownBy(() -> alarmService.update(invalidAlarmId, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 알람입니다.");
    }
}
