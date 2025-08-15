package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.fcm.FcmMessageConverter;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
@Import({
        TeamAssignedItemService.class,
        FcmMessageConverter.class,
        JpaAuditingConfig.class,
})
class TeamAssignedItemServiceTest {

    @Autowired
    private TeamAssignedItemService teamAssignedItemService;

    @MockitoBean
    private FcmMessageSender fcmMessageSender;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리 담당 물품을 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);
            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(anotherTeamMember);

            final String itemName = "담당 물품";
            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    itemName,
                    List.of(member.getId())
            );

            // when
            final Long actual = teamAssignedItemService.create(teamMember, request);

            // then
            final TeamAssignedItemInfo teamAssignedItemInfo = entityManager.find(TeamAssignedItemInfo.class, actual);
            final List<TeamAssignedItem> teamAssignedItems = entityManager.createQuery(
                            "SELECT tai FROM TeamAssignedItem tai WHERE tai.info.id = :infoId", TeamAssignedItem.class)
                    .setParameter("infoId", actual)
                    .getResultList();
            // anotherMember가 담당하지 않으므로, teamAssignedItemsByMember는 비어있다.
            final List<TeamAssignedItem> teamAssignedItemsByMember = entityManager.createQuery(
                            "SELECT tai FROM TeamAssignedItem tai WHERE tai.teamMember.id = :teamMemberId",
                            TeamAssignedItem.class)
                    .setParameter("teamMemberId", anotherTeamMember.getId())
                    .getResultList();
            assertAll(
                    () -> assertThat(teamAssignedItemInfo).isNotNull(),
                    () -> assertThat(teamAssignedItemInfo.getName()).isEqualTo(itemName),
                    () -> assertThat(teamAssignedItems).hasSize(1),
                    () -> assertThat(teamAssignedItems.getFirst().getInfo()).isEqualTo(teamAssignedItemInfo),
                    () -> assertThat(teamAssignedItems.getFirst().getTeamMember()).isEqualTo(teamMember),
                    () -> assertThat(teamAssignedItemsByMember).hasSize(0)
            );
        }

        @DisplayName("팀 보따리 담당 물품을 생성 시, 이미 존재하는 물품 이름이라면, 예외를 던진다.")
        @Test
        void create_Exception_AlreadyExistsName() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "담당 물품";
            entityManager.persist(new TeamAssignedItemInfo(itemName, teamBottari));

            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    itemName,
                    List.of(member.getId())
            );

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 존재하는 팀 보따리 물품입니다. - 담당");
        }

        @DisplayName("팀 보따리 담당 물품을 생성 시, 팀에 속하지 않는 멤버를 요청한다면, 예외를 던진다.")
        @Test
        void create_Exception_NotInTeam() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "담당 물품";
            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    itemName,
                    List.of(anotherMember.getId())
            );

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다. - 요청된 팀원 중 일부가 팀에 속해 있지 않습니다.");
        }

        @DisplayName("팀 보따리 담당 물품을 생성 시, 팀에 속하지 않는 멤버를 요청한다면, 예외를 던진다.")
        @Test
        void create_Exception_NotFoundMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "담당 물품";
            final Long invalidMemberId = -1L;
            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    itemName,
                    List.of(invalidMemberId)
            );

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 요청된 팀원 중 일부가 존재하지 않습니다.");
        }

        @DisplayName("팀 보따리 담당 물품 생성 시, 담당 멤버가 없다면, 예외를 던진다.")
        @Test
        void create_Exception_NoAssignedMembers() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "담당 물품";
            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    itemName,
                    List.of()
            );

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 담당 물품에 팀원이 지정되지 않았습니다.");
        }
    }

    @Nested
    class DeleteTest {

        @DisplayName("팀 보따리 담당 물품을 삭제한다.")
        @Test
        void delete() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            // when
            teamAssignedItemService.delete(teamAssignedItem.getId(), member.getSsaid());

            // then
            final TeamAssignedItem actualItem = entityManager.find(TeamAssignedItem.class, teamAssignedItem.getId());
            final TeamAssignedItemInfo actualInfo = entityManager.find(TeamAssignedItemInfo.class,
                    teamAssignedItemInfo.getId());
            assertAll(
                    () -> assertThat(actualItem).isNull(),
                    () -> assertThat(actualInfo).isNull()
            );
        }

        @DisplayName("팀 보따리 담당 물품 삭제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void delete_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = -1L;

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.delete(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 담당");
        }
    }

    @Nested
    class GetAllByTeamMemberTest {

        @DisplayName("팀 멤버를 통해 물품을 조회한다.")
        @Test
        void getAllByTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            // when
            final List<TeamMemberItemResponse> actual = teamAssignedItemService.getAllByTeamMember(teamMember);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.getFirst().name()).isEqualTo(teamAssignedItemInfo.getName())
            );
        }
    }

    @Nested
    class CheckTest {

        @DisplayName("팀 보따리 담당 물품을 체크한다.")
        @Test
        void check() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            // when
            teamAssignedItemService.check(teamAssignedItem.getId(), member.getSsaid());

            // then
            final TeamAssignedItem actual = entityManager.find(TeamAssignedItem.class, teamAssignedItem.getId());
            assertThat(actual.isChecked()).isTrue();
        }

        @DisplayName("팀 보따리 담당 물품 체크 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void check_Exception_NotOwned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.check(teamAssignedItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 담당 물품 체크 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void check_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.check(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 담당");
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("팀 보따리 담당 물품을 체크 해제한다.")
        @Test
        void uncheck() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            teamAssignedItem.check();
            entityManager.persist(teamAssignedItem);

            // when
            teamAssignedItemService.uncheck(teamAssignedItem.getId(), member.getSsaid());

            // then
            final TeamAssignedItem actual = entityManager.find(TeamAssignedItem.class, teamAssignedItem.getId());
            assertThat(actual.isChecked()).isFalse();
        }

        @DisplayName("팀 보따리 담당 물품 체크 해제 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotOwned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.uncheck(teamAssignedItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 담당 물품 체크 해제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.uncheck(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 담당");
        }
    }

    @Nested
    class SendRemindAlarmTest {

        @DisplayName("해당 물품을 챙기지 않은 팀 멤버에게 알람을 보낸다.")
        @Test
        void sendRemindAlarm() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            final List<Long> uncheckedMemberIds = List.of(
                    member.getId()
            );

            doNothing().when(fcmMessageSender).sendMessageToMembers(eq(uncheckedMemberIds), any());

            // when & then
            assertThatCode(
                    () -> teamAssignedItemService.sendRemindAlarm(teamAssignedItemInfo.getId(), member.getSsaid()))
                    .doesNotThrowAnyException();
            verify(fcmMessageSender).sendMessageToMembers(eq(uncheckedMemberIds), any());
        }

        @DisplayName("보채기 알람을 보낼 때, 물품 정보가 존재하지 않는다면 예외를 던진다.")
        @Test
        void sendRemindAlarm_Exception_NotFountItemInfo() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long invalid_item_info_id = -1L;

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.sendRemindAlarm(invalid_item_info_id, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품 정보를 찾을 수 없습니다. - 담당");
        }

        @DisplayName("보채기 알람을 보낼 때, 유효하지 않은 ssaid라면 예외를 던진다.")
        @Test
        void sendRemindAlarm_Exception_InvalidSsaid() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo info = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(info);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(info, teamMember);
            entityManager.persist(teamAssignedItem);

            final String invalid_ssaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(
                    () -> teamAssignedItemService.sendRemindAlarm(info.getId(), invalid_ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("보채기 알람을 보낼 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void sendRemindAlarm_Exception_NotTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo info = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(info);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(info, teamMember);
            entityManager.persist(teamAssignedItem);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            // when & then
            assertThatThrownBy(
                    () -> teamAssignedItemService.sendRemindAlarm(info.getId(), anotherMember.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }
}
