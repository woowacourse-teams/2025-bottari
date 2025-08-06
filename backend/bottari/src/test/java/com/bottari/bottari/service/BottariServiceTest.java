package com.bottari.bottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.alarm.domain.Alarm;
import com.bottari.alarm.domain.LocationAlarm;
import com.bottari.alarm.domain.RepeatType;
import com.bottari.alarm.domain.RoutineAlarm;
import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.bottari.dto.CreateBottariRequest;
import com.bottari.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.bottari.dto.ReadBottariResponse;
import com.bottari.bottari.dto.UpdateBottariRequest;
import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.fixture.AlarmFixture;
import com.bottari.fixture.BottariFixture;
import com.bottari.fixture.BottariItemFixture;
import com.bottari.fixture.LocationAlarmFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.RoutineAlarmFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class GetByIdTest {

        @DisplayName("보따리를 상세 조회한다.")
        @Test
        void getById() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final Bottari anotherBottari = BottariFixture.ANOTHER_BOTTARI.get(member);
            entityManager.persist(anotherBottari);

            final BottariItem bottariItem = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottariItem);

            final RoutineAlarm routineAlarm = RoutineAlarmFixture.EVERY_WEEK_REPEAT_ALARM.get();
            final LocationAlarm locationAlarm = LocationAlarmFixture.LOCATION_ALARM_ON.get();
            final Alarm alarm = AlarmFixture.ALARM_ON.get(routineAlarm, locationAlarm, bottari);
            entityManager.persist(alarm);

            // when
            final ReadBottariResponse actual = bottariService.getById(member.getSsaid(), bottari.getId());

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(bottari.getId()),
                    () -> assertThat(actual.items()).hasSize(1),
                    () -> assertThat(actual.alarm()).isNotNull(),
                    () -> assertThat(actual.alarm().id()).isEqualTo(alarm.getId()),
                    () -> assertThat(actual.alarm().isActive()).isTrue(),
                    () -> assertThat(actual.alarm().routine().type()).isEqualTo(RepeatType.EVERY_WEEK_REPEAT),
                    () -> assertThat(actual.alarm().location().latitude()).isEqualTo(locationAlarm.latitude())
            );
        }

        @DisplayName("본인의 보따리가 아닌 보따리를 조회할 경우, 예외를 던진다.")
        @Test
        void getById_Exception_NotOwner() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            // when & then
            assertThatThrownBy(() -> bottariService.getById("invalid_ssaid", bottari.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 보따리에 접근할 수 있는 권한이 없습니다. - 본인의 보따리가 아닙니다.");
        }

        @DisplayName("존재하지 않는 id로 보따리를 조회할 경우, 예외를 던진다.")
        @Test
        void getById_Exception_NotFound() {
            // when & then
            assertThatThrownBy(() -> bottariService.getById("ssaid", 1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("보따리를 찾을 수 없습니다.");
        }
    }

    @Nested
    class GetAllBySsaidSortedByLatestTest {

        @DisplayName("사용자의 모든 보따리를 최신순으로 조회한다.")
        @Test
        void getAllBySsaidSortedByLatest() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final Bottari anotherBottari = BottariFixture.ANOTHER_BOTTARI.get(anotherMember);
            entityManager.persist(anotherBottari);

            final BottariItem bottari1Item1 = BottariItemFixture.BOTTARI_ITEM_1.get(bottari);
            entityManager.persist(bottari1Item1);

            final BottariItem bottari1Item2 = BottariItemFixture.BOTTARI_ITEM_2.get(bottari);
            bottari1Item2.check();
            entityManager.persist(bottari1Item2);

            final Bottari bottari2 = BottariFixture.BOTTARI_2.get(member);
            entityManager.persist(bottari2);

            final BottariItem bottari2Item1 = BottariItemFixture.BOTTARI_ITEM_1.get(bottari2);
            entityManager.persist(bottari2Item1);

            final RoutineAlarm routineAlarm = RoutineAlarmFixture.NON_REPEAT_ALARM.get();
            final LocationAlarm locationAlarm = LocationAlarmFixture.LOCATION_ALARM_OFF.get();
            final Alarm alarm = AlarmFixture.ALARM_ON.get(routineAlarm, locationAlarm, bottari);
            entityManager.persist(alarm);

            // when
            final List<ReadBottariPreviewResponse> actual = bottariService.getAllBySsaidSortedByLatest(
                    member.getSsaid());

            // then
            assertAll(
                    () -> assertThat(actual).extracting("title").containsExactly(
                            bottari2.getTitle(),
                            bottari.getTitle()
                    ),
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
    }

    @Nested
    class CreateTest {

        @DisplayName("보따리를 생성한다.")
        @Test
        void create() {
            // given
            final CreateBottariRequest request = new CreateBottariRequest("title");
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            // when
            final Long actual = bottariService.create(member.getSsaid(), request);

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
    }

    @Nested
    class DeleteTest {

        @DisplayName("아이디를 통해 보따리를 삭제한다.")
        @Test
        void deleteById() {
            // given
            final String ssaid = "ssaid";
            final Member member = new Member(ssaid, "name");
            entityManager.persist(member);

            final Bottari delete_bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(delete_bottari);

            final Bottari remain_bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(remain_bottari);

            final BottariItem bottariItem1 = BottariItemFixture.BOTTARI_ITEM_1.get(delete_bottari);
            final BottariItem bottariItem2 = BottariItemFixture.BOTTARI_ITEM_2.get(delete_bottari);
            entityManager.persist(bottariItem1);
            entityManager.persist(bottariItem2);

            final RoutineAlarm routineAlarm = RoutineAlarmFixture.EVERY_WEEK_REPEAT_ALARM.get();
            final LocationAlarm locationAlarm = LocationAlarmFixture.LOCATION_ALARM_ON.get();
            final Alarm alarm = AlarmFixture.ALARM_ON.get(routineAlarm, locationAlarm, delete_bottari);
            entityManager.persist(alarm);

            // when
            bottariService.deleteById(delete_bottari.getId(), ssaid);

            // then
            final Bottari remainingBottari = entityManager.find(Bottari.class, remain_bottari.getId());
            final Bottari deletedBottari = entityManager.find(Bottari.class, delete_bottari.getId());

            assertAll(
                    () -> assertThat(deletedBottari).isNull(),
                    () -> assertThat(remainingBottari).isNotNull(),
                    () -> assertThat(remainingBottari.getTitle()).isEqualTo(remainingBottari.getTitle())
            );
        }
    }

    @Nested
    class UpdateTest {

        @DisplayName("보따리의 제목을 수정한다.")
        @Test
        void update() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Bottari bottari = BottariFixture.BOTTARI.get(member);
            entityManager.persist(bottari);

            final UpdateBottariRequest request = new UpdateBottariRequest("updated_title");

            // when
            bottariService.update(request, bottari.getId(), member.getSsaid());

            // then
            final Bottari updatedBottari = entityManager.find(Bottari.class, bottari.getId());
            assertThat(updatedBottari.getTitle()).isEqualTo("updated_title");
        }

        @DisplayName("본인의 보따리가 아닌 보따리를 수정할 경우, 예외를 던진다.")
        @Test
        void update_Exception_NotMine() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final Bottari bottari = BottariFixture.BOTTARI.get(anotherMember);
            entityManager.persist(bottari);

            final UpdateBottariRequest request = new UpdateBottariRequest("updated_title");

            // when & then
            assertThatThrownBy(() -> bottariService.update(request, bottari.getId(), "invalid_ssaid"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 보따리에 접근할 수 있는 권한이 없습니다. - 본인의 보따리가 아닙니다.");
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
    }

    @Nested
    class DeleteByIdTest {

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
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final Bottari anotherMemberBottari = BottariFixture.BOTTARI.get(anotherMember);
            entityManager.persist(anotherMemberBottari);

            // when & then
            assertThatThrownBy(() -> bottariService.deleteById(anotherMemberBottari.getId(), "ssaid"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 보따리에 접근할 수 있는 권한이 없습니다. - 본인의 보따리가 아닙니다.");
        }
    }
}
