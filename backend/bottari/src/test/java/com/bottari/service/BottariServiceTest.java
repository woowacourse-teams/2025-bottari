package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.Member;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import com.bottari.dto.CreateBottariRequest;
import com.bottari.dto.ReadBottariResponse;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BottariService.class)
class BottariServiceTest {

    @Autowired
    private BottariService bottariService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("보따리를 상세 조회한다.")
    @Test
    void getById() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        final Bottari anotherBottari = new Bottari("another_title", member);
        entityManager.persist(anotherBottari);

        final BottariItem bottariItem = new BottariItem("item", bottari);
        entityManager.persist(bottariItem);

        final RoutineAlarm routineAlarm = new RoutineAlarm(
                LocalTime.now(),
                RepeatType.EVERY_WEEK_REPEAT,
                null,
                Set.of(DayOfWeek.MONDAY)
        );
        final LocationAlarm locationAlarm = new LocationAlarm(true, 37.5, 127.5, 100);
        final Alarm alarm = new Alarm(true, routineAlarm, locationAlarm, bottari);
        entityManager.persist(alarm);

        // when
        final ReadBottariResponse actual = bottariService.getById(ssaid, bottari.getId());

        // then
        assertAll(
                () -> assertThat(actual.id()).isEqualTo(bottari.getId()),
                () -> assertThat(actual.items()).hasSize(1),
                () -> assertThat(actual.alarm()).isNotNull(),
                () -> assertThat(actual.alarm().routine().type()).isEqualTo(RepeatType.EVERY_WEEK_REPEAT),
                () -> assertThat(actual.alarm().location().latitude()).isEqualTo(37.5)
        );
    }

    @DisplayName("본인의 보따리가 아닌 보따리를 조회할 경우, 예외를 던진다.")
    @Test
    void getById_Exception_NotOwner() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("title", member);
        entityManager.persist(bottari);

        // when & then
        assertThatThrownBy(() -> bottariService.getById("invalid_ssaid", bottari.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 보따리가 아닙니다.");
    }

    @DisplayName("존재하지 않는 id로 보따리를 조회할 경우, 예외를 던진다.")
    @Test
    void getById_Exception_NotFound() {
        // when & then
        assertThatThrownBy(() -> bottariService.getById("ssaid", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리를 찾을 수 없습니다.");
    }

    @DisplayName("보따리를 생성한다.")
    @Test
    void create() {
        // given
        final CreateBottariRequest request = new CreateBottariRequest("title");
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        // when
        final Long actual = bottariService.create(ssaid, request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하지 않는 ssaid로 보따리를 생성할 경우, 예외를 던진다.")
    @Test
    void create_Exception_NotExistsSsaid() {
        // given
        final CreateBottariRequest request = new CreateBottariRequest("title");
        final String ssaid = "ssaid";

        // when & then
        assertThatThrownBy(() -> bottariService.create(ssaid, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 ssaid로 가입된 사용자가 없습니다.");
    }
}
