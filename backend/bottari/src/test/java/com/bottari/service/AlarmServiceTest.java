package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.domain.Bottari;
import com.bottari.domain.Member;
import com.bottari.domain.RepeatType;
import com.bottari.dto.CreateAlarmRequest;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
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
}
