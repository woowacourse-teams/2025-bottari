package com.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.Member;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import com.bottari.dto.CreateBottariRequest;
import com.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.dto.ReadBottariResponse;
import com.bottari.dto.UpdateBottariRequest;
import com.bottari.error.BusinessException;
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
@Import({BottariService.class, JpaAuditingConfig.class})
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
                () -> assertThat(actual.alarm().id()).isEqualTo(alarm.getId()),
                () -> assertThat(actual.alarm().isActive()).isTrue(),
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
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리를 찾을 수 없습니다.");
    }

    @DisplayName("사용자의 모든 보따리를 최신순으로 조회한다.")
    @Test
    void getAllBySsaidSortedByLatest() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari1 = new Bottari("title1", member);
        entityManager.persist(bottari1);

        final Member anotherMember = new Member("another_ssaid", "another");
        entityManager.persist(anotherMember);

        final Bottari anotherBottari = new Bottari("another_title", anotherMember);
        entityManager.persist(anotherBottari);

        final BottariItem bottari1Item1 = new BottariItem("item1", bottari1);
        entityManager.persist(bottari1Item1);

        final BottariItem bottari1Item2 = new BottariItem("item2", bottari1);
        bottari1Item2.check();
        entityManager.persist(bottari1Item2);

        final Bottari bottari2 = new Bottari("title2", member);
        entityManager.persist(bottari2);

        final BottariItem bottari2Item1 = new BottariItem("item1", bottari2);
        entityManager.persist(bottari2Item1);

        final RoutineAlarm routineAlarm = new RoutineAlarm(LocalTime.MAX, RepeatType.NON_REPEAT, LocalDate.now(), null);
        final Alarm alarm = new Alarm(true, routineAlarm, null, bottari1);
        entityManager.persist(alarm);

        // when
        final List<ReadBottariPreviewResponse> actual = bottariService.getAllBySsaidSortedByLatest(ssaid);

        // then
        assertAll(
                () -> assertThat(actual).extracting("title").containsExactly("title2", "title1"),
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.getFirst().totalItemsCount()).isEqualTo(1),
                () -> assertThat(actual.getFirst().checkedItemsCount()).isEqualTo(0),
                () -> assertThat(actual.getFirst().alarm()).isNull(),
                () -> assertThat(actual.get(1).totalItemsCount()).isEqualTo(2),
                () -> assertThat(actual.get(1).checkedItemsCount()).isEqualTo(1),
                () -> assertThat(actual.get(1).alarm()).isNotNull(),
                () -> assertThat(actual.get(1).alarm().id()).isEqualTo(alarm.getId()),
                () -> assertThat(actual.get(1).alarm().isActive()).isTrue()
        );
    }

    @DisplayName("존재하지 않는 사용자의 모든 보따리를 조회할 경우, 예외를 던진다.")
    @Test
    void getAllBySsaidSortedByLatest_SortedByLatest_Exception_NotFoundMember() {
        // given
        final String ssaid = "invalid_ssaid";

        // when & then
        assertThatThrownBy(() -> bottariService.getAllBySsaidSortedByLatest(ssaid))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
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
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
    }

    @DisplayName("아이디를 통해 보따리를 삭제한다.")
    @Test
    void deleteById() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari delete_bottari = new Bottari("delete_bottari", member);
        entityManager.persist(delete_bottari);

        final Bottari remain_bottari = new Bottari("remain_bottari", member);
        entityManager.persist(remain_bottari);

        final BottariItem bottariItem1 = new BottariItem("bottari1_item1", delete_bottari);
        final BottariItem bottariItem2 = new BottariItem("bottari1_item2", delete_bottari);
        entityManager.persist(bottariItem1);
        entityManager.persist(bottariItem2);

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
        final Alarm alarm = new Alarm(true, routineAlarm, locationAlarm, delete_bottari);
        entityManager.persist(alarm);

        // when
        bottariService.deleteById(delete_bottari.getId(), ssaid);

        // then
        final Bottari remainingBottari = entityManager.find(Bottari.class, remain_bottari.getId());
        final Bottari deletedBottari = entityManager.find(Bottari.class, delete_bottari.getId());

        assertAll(
                () -> assertThat(deletedBottari).isNull(),
                () -> assertThat(remainingBottari).isNotNull(),
                () -> assertThat(remainingBottari.getTitle()).isEqualTo("remain_bottari")
        );
    }

    @DisplayName("보따리의 제목을 수정한다.")
    @Test
    void update() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Bottari bottari = new Bottari("original_title", member);
        entityManager.persist(bottari);

        final UpdateBottariRequest request = new UpdateBottariRequest("updated_title");

        // when
        bottariService.update(request, bottari.getId(), ssaid);

        // then
        final Bottari updatedBottari = entityManager.find(Bottari.class, bottari.getId());
        assertThat(updatedBottari.getTitle()).isEqualTo("updated_title");
    }

    @DisplayName("본인의 보따리가 아닌 보따리를 수정할 경우, 예외를 던진다.")
    @Test
    void update_Exception_NotMine() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "member_1");
        entityManager.persist(member);

        final Member anotherMember = new Member("another_ssaid", "member_2");
        entityManager.persist(anotherMember);

        final Bottari bottari = new Bottari("title", anotherMember);
        entityManager.persist(bottari);

        final UpdateBottariRequest request = new UpdateBottariRequest("updated_title");

        // when & then
        assertThatThrownBy(() -> bottariService.update(request, bottari.getId(), "invalid_ssaid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 보따리가 아닙니다.");
    }

    @DisplayName("존재하지 않는 id로 보따리를 수정할 경우, 예외를 던진다.")
    @Test
    void update_Exception_NotFound() {
        // given
        final Long invalid_bottari_id = -1L;
        final UpdateBottariRequest request = new UpdateBottariRequest("updated_title");

        // when & then
        assertThatThrownBy(() -> bottariService.update(request, invalid_bottari_id, "ssaid"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리를 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 보따리를 삭제할 경우, 예외를 던진다.")
    @Test
    void deleteById_Exception_NotFound() {
        // given
        final Long invalid_bottari_id = -1L;

        // when & then
        assertThatThrownBy(() -> bottariService.deleteById(invalid_bottari_id, "ssaid"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리를 찾을 수 없습니다.");
    }

    @DisplayName("본인의 것이 아닌 보따리를 삭제할 경우, 예외를 던진다.")
    @Test
    void deleteById_Exception_NotMine() {
        // given
        final String ssaid = "ssaid";
        final Member member = new Member(ssaid, "name");
        entityManager.persist(member);

        final Member anotherMember = new Member("another_ssaid", "name_2");
        entityManager.persist(anotherMember);

        final Bottari anotherMemberBottari = new Bottari("title1", anotherMember);
        entityManager.persist(anotherMemberBottari);

        // when & then
        assertThatThrownBy(() -> bottariService.deleteById(anotherMemberBottari.getId(), "ssaid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 보따리가 아닙니다.");
    }
}
