package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import com.bottari.teambottari.domain.TeamItemType;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
@Import({
        TeamItemFacade.class,
        TeamSharedItemService.class,
        TeamAssignedItemService.class,
        TeamPersonalItemService.class,
        FcmMessageConverter.class,
        JpaAuditingConfig.class
})
public class TeamItemFacadeTest {

    @Autowired
    private TeamItemFacade teamItemFacade;

    @MockitoBean
    private FcmMessageSender fcmMessageSender;

    @Autowired
    private EntityManager entityManager;

    private Long createItemByType(
            final TeamItemType type,
            final TeamBottari teamBottari,
            final TeamMember teamMember
    ) {
        return switch (type) {
            case SHARED -> {
                final TeamSharedItemInfo itemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
                entityManager.persist(itemInfo);
                final TeamSharedItem item = new TeamSharedItem(itemInfo, teamMember);
                entityManager.persist(item);
                yield itemInfo.getId();
            }
            case ASSIGNED -> {
                final TeamAssignedItemInfo itemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
                entityManager.persist(itemInfo);
                final TeamAssignedItem item = new TeamAssignedItem(itemInfo, teamMember);
                entityManager.persist(item);
                yield itemInfo.getId();
            }
            case PERSONAL -> {
                final TeamPersonalItem item = new TeamPersonalItem("개인 물품", teamMember);
                entityManager.persist(item);
                yield item.getId();
            }
        };
    }

    @Nested
    class GetSharedItemsTest {

        @DisplayName("공통 물품을 조회한다.")
        @Test
        void getSharedItems() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamSharedItemInfo itemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
            entityManager.persist(itemInfo);
            final TeamSharedItem item = new TeamSharedItem(itemInfo, teamMember);
            entityManager.persist(item);

            final String ssaid = member.getSsaid();

            // when
            final List<ReadSharedItemResponse> actual = teamItemFacade.getSharedItems(teamBottari.getId(), ssaid);

            // then
            final List<ReadSharedItemResponse> expected = List.of(
                    new ReadSharedItemResponse(
                            itemInfo.getId(),
                            itemInfo.getName()
                    )
            );
            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

        @DisplayName("공통 물품을 조회할 때, 팀 보따리가 존재하지 않는다면 예외를 던진다.")
        @Test
        void getSharedItems_Exception_NotFoundTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalid_teamBottariId = -1L;
            final String ssaid = member.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getSharedItems(invalid_teamBottariId, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다. - 존재하지 않는 팀 보따리입니다.");
        }

        @DisplayName("공통 물품을 조회할 때, 가입되지 않은 멤버의 ssaid를 입력하면 예외를 던진다.")
        @Test
        void getSharedItems_Exception_NotMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getSharedItems(teamBottari.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("공통 물품을 조회할 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void getSharedItems_Exception_NotTeamMember() {
            // given
            final Member member_in_team = MemberFixture.MEMBER.get();
            final Member member_not_in_team = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_in_team);

            entityManager.persist(member_not_in_team);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_in_team);
            entityManager.persist(teamBottari);
            final String ssaid = member_not_in_team.getSsaid();
            // when & then
            assertThatThrownBy(() -> teamItemFacade.getSharedItems(teamBottari.getId(), ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class CreateSharedItemTest {

        @DisplayName("공통 물품을 생성한다.")
        @Test
        void createSharedItem() {
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

            final String itemName = "공통 물품";
            final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);
            final String ssaid = member.getSsaid();

            // when
            final Long actual = teamItemFacade.createSharedItem(teamBottari.getId(), request, ssaid);

            // then
            final TeamSharedItemInfo actualItemInfo = entityManager.find(TeamSharedItemInfo.class, actual);
            final List<TeamSharedItem> actualSharedItems = entityManager
                    .createQuery("SELECT tsi FROM TeamSharedItem tsi WHERE tsi.info.id = :infoId", TeamSharedItem.class)
                    .setParameter("infoId", actualItemInfo.getId())
                    .getResultList();
            assertAll(
                    () -> assertThat(actualItemInfo.getName()).isEqualTo(itemName),
                    () -> assertThat(actualSharedItems).hasSize(2)
            );
        }

        @DisplayName("공통 물품을 생성할 때, 팀 보따리가 존재하지 않는다면 예외를 던진다.")
        @Test
        void createSharedItem_Exception_NotFoundTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalid_teamBottariId = -1L;
            final CreateTeamItemRequest request = new CreateTeamItemRequest("공통 물품");
            final String ssaid = member.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.createSharedItem(invalid_teamBottariId, request, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("공통 물품을 생성할 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void createSharedItem_Exception_NotTeamMember() {
            // given
            final Member member_in_team = MemberFixture.MEMBER.get();
            final Member member_not_in_team = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_in_team);
            entityManager.persist(member_not_in_team);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_in_team);
            entityManager.persist(teamBottari);

            final CreateTeamItemRequest request = new CreateTeamItemRequest("공통 물품");
            final String ssaid = member_not_in_team.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.createSharedItem(teamBottari.getId(), request, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class CreateAssignedItemTest {

        @DisplayName("담당 물품을 생성한다.")
        @Test
        void createAssignedItem() {
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
                    List.of(member.getId(), anotherMember.getId())
            );
            final String ssaid = member.getSsaid();

            // when
            final Long actual = teamItemFacade.createAssignedItem(teamBottari.getId(), request, ssaid);

            // then
            final TeamAssignedItemInfo actualItemInfo = entityManager.find(TeamAssignedItemInfo.class, actual);
            final List<TeamAssignedItem> actualAssignedItems = entityManager
                    .createQuery("SELECT tai FROM TeamAssignedItem tai WHERE tai.info.id = :infoId",
                            TeamAssignedItem.class)
                    .setParameter("infoId", actualItemInfo.getId())
                    .getResultList();
            assertAll(
                    () -> assertThat(actualItemInfo.getName()).isEqualTo(itemName),
                    () -> assertThat(actualAssignedItems).hasSize(2)
            );
        }

        @DisplayName("담당 물품을 생성할 때, 팀 보따리가 존재하지 않는다면 예외를 던진다.")
        @Test
        void createAssignedItem_Exception_NotFoundTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalid_teamBottariId = -1L;
            final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                    "담당 물품",
                    List.of(member.getId())
            );
            final String ssaid = member.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.createAssignedItem(invalid_teamBottariId, request, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }
    }

    @Nested
    class CreatePersonalItemTest {

        @DisplayName("개인 물품을 생성한다.")
        @Test
        void createPersonalItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);
            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "개인 물품";
            final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);
            final String ssaid = member.getSsaid();

            // when
            final Long actual = teamItemFacade.createPersonalItem(teamBottari.getId(), request, ssaid);

            // then
            final TeamPersonalItem actualItem = entityManager.find(TeamPersonalItem.class, actual);
            assertThat(actualItem.getName()).isEqualTo(itemName);
        }

        @DisplayName("개인 물품을 생성할 때, 팀 보따리가 존재하지 않는다면 예외를 던진다.")
        @Test
        void createPersonalItem_Exception_NotFoundTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalid_teamBottariId = -1L;
            final CreateTeamItemRequest request = new CreateTeamItemRequest("개인 물품");
            final String ssaid = member.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.createPersonalItem(invalid_teamBottariId, request, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("개인 물품을 생성할 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void createPersonalItem_Exception_NotTeamMember() {
            // given
            final Member member_in_team = MemberFixture.MEMBER.get();
            final Member member_not_in_team = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_in_team);
            entityManager.persist(member_not_in_team);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_in_team);
            entityManager.persist(teamBottari);

            final CreateTeamItemRequest request = new CreateTeamItemRequest("개인 물품");
            final String ssaid = member_not_in_team.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.createPersonalItem(teamBottari.getId(), request, ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("물품 삭제 테스트")
    class DeleteItemTest {

        @DisplayName("공통 물품을 삭제하면 관련된 모든 공통 물품이 삭제된다.")
        @Test
        void deleteSharedItem() {
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

            final Long sharedItemInfoId = createItemByType(TeamItemType.SHARED, teamBottari, teamMember);
            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.SHARED);
            final String ssaid = member.getSsaid();

            // when
            teamItemFacade.delete(sharedItemInfoId, ssaid, request);
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamSharedItemInfo actualItemInfo = entityManager.find(TeamSharedItemInfo.class, sharedItemInfoId);
            final List<TeamSharedItem> actualSharedItems = entityManager
                    .createQuery("SELECT tsi FROM TeamSharedItem tsi WHERE tsi.info.id = :infoId", TeamSharedItem.class)
                    .setParameter("infoId", sharedItemInfoId)
                    .getResultList();

            assertAll(
                    () -> assertThat(actualItemInfo).isNull(),
                    () -> assertThat(actualSharedItems).isEmpty()
            );
        }

        @DisplayName("개인 물품을 삭제한다.")
        @Test
        void deletePersonalItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long personalItemId = createItemByType(TeamItemType.PERSONAL, teamBottari, teamMember);
            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.PERSONAL);
            final String ssaid = member.getSsaid();

            // when
            teamItemFacade.delete(personalItemId, ssaid, request);
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamPersonalItem actual = entityManager.find(TeamPersonalItem.class, personalItemId);
            assertThat(actual).isNull();
        }

        @DisplayName("다른 사람의 개인 물품을 삭제하려고 하면 예외를 던진다.")
        @Test
        void deletePersonalItem_Exception_NotOwner() {
            // given
            final Member owner = MemberFixture.MEMBER.get();
            entityManager.persist(owner);
            final Member other = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(other);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(owner);
            entityManager.persist(teamBottari);

            final TeamMember ownerTeamMember = new TeamMember(teamBottari, owner);
            entityManager.persist(ownerTeamMember);
            final TeamMember otherTeamMember = new TeamMember(teamBottari, other);
            entityManager.persist(otherTeamMember);

            final Long personalItemId = createItemByType(TeamItemType.PERSONAL, teamBottari, ownerTeamMember);
            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.PERSONAL);
            final String ssaidOfOther = other.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.delete(personalItemId, ssaidOfOther, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 멤버가 아닌 사람이 공통 물품을 삭제하려고 하면 예외를 던진다.")
        @Test
        void deleteSharedItem_Exception_NotTeamMember() {
            // given
            final Member memberInTeam = MemberFixture.MEMBER.get();
            entityManager.persist(memberInTeam);
            final Member memberNotInTeam = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(memberNotInTeam);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(memberInTeam);
            entityManager.persist(teamBottari);
            final TeamMember teamMember = new TeamMember(teamBottari, memberInTeam);
            entityManager.persist(teamMember);

            final Long sharedItemInfoId = createItemByType(TeamItemType.SHARED, teamBottari, teamMember);
            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.SHARED);
            final String ssaidOfMemberNotInTeam = memberNotInTeam.getSsaid();

            // when & then
            assertThatThrownBy(() -> teamItemFacade.delete(sharedItemInfoId, ssaidOfMemberNotInTeam, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }

        @DisplayName("존재하지 않는 물품을 삭제하려고 하면 예외를 던진다.")
        @ParameterizedTest
        @CsvSource({
                "SHARED, 공통",
                "ASSIGNED, 담당",
                "PERSONAL, 개인"
        })
        void deleteItem_Exception_NotFound(
                final TeamItemType type,
                final String typeName
        ) {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final String ssaid = member.getSsaid();
            final Long invalidItemId = -1L;
            final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

            // when & then
            assertThatThrownBy(() -> teamItemFacade.delete(invalidItemId, ssaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - " + typeName);
        }
    }

    @Nested
    class GetTeamItemStatusTest {

        @DisplayName("팀 보따리 물품 현황을 조회한다.")
        @Test
        void getTeamItemStatus() {
            // given
            final Member member_1 = MemberFixture.MEMBER.get();
            final Member member_2 = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_1);
            entityManager.persist(member_2);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_1);
            entityManager.persist(teamBottari);

            final TeamMember teamMember_1 = new TeamMember(teamBottari, member_1);
            final TeamMember teamMember_2 = new TeamMember(teamBottari, member_2);
            entityManager.persist(teamMember_1);
            entityManager.persist(teamMember_2);

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            // 공통 물품 배정
            final TeamSharedItem member_1_sharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember_1);
            final TeamSharedItem member_2_sharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember_2);
            entityManager.persist(member_1_sharedItem);
            entityManager.persist(member_2_sharedItem);

            // 담당 물품 배정
            final TeamAssignedItemInfo teamAssignedItemInfo_1 = new TeamAssignedItemInfo("멤버 1 물품", teamBottari);
            final TeamAssignedItemInfo teamAssignedItemInfo_2 = new TeamAssignedItemInfo("멤버 2 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo_1);
            entityManager.persist(teamAssignedItemInfo_2);

            // 담당 물품 배정 (멤버 1 -> member_1_assignedItem, 멤버 2 -> member_2_assignedItem)
            final TeamAssignedItem member_1_assignedItem = new TeamAssignedItem(teamAssignedItemInfo_1, teamMember_1);
            final TeamAssignedItem member_2_assignedItem = new TeamAssignedItem(teamAssignedItemInfo_2, teamMember_2);
            member_2_assignedItem.check();
            entityManager.persist(member_1_assignedItem);
            entityManager.persist(member_2_assignedItem);

            // when
            final ReadTeamItemStatusResponse actual = teamItemFacade.getTeamItemStatus(teamBottari.getId(),
                    member_1.getSsaid());

            // then
            final List<TeamItemStatusResponse.MemberCheckStatusResponse> expectedSharedMemberStatus = List.of(
                    new TeamItemStatusResponse.MemberCheckStatusResponse(member_1.getName(), false),
                    new TeamItemStatusResponse.MemberCheckStatusResponse(member_2.getName(), false)
            );
            final List<TeamItemStatusResponse.MemberCheckStatusResponse> expectedMember1AssignedStatus = List.of(
                    new TeamItemStatusResponse.MemberCheckStatusResponse(member_1.getName(), false)
            );
            final List<TeamItemStatusResponse.MemberCheckStatusResponse> expectedMember2AssignedStatus = List.of(
                    new TeamItemStatusResponse.MemberCheckStatusResponse(member_2.getName(), true)
            );

            final List<TeamItemStatusResponse> expectedSharedItems = List.of(
                    new TeamItemStatusResponse(teamSharedItemInfo.getId(), "공통 물품", expectedSharedMemberStatus, 0, 2)
            );
            final List<TeamItemStatusResponse> expectedAssignedItems = List.of(
                    new TeamItemStatusResponse(
                            teamAssignedItemInfo_1.getId(), "멤버 1 물품", expectedMember1AssignedStatus, 0, 1),
                    new TeamItemStatusResponse(
                            teamAssignedItemInfo_2.getId(), "멤버 2 물품", expectedMember2AssignedStatus, 1, 1)
            );
            final ReadTeamItemStatusResponse expected = new ReadTeamItemStatusResponse(expectedSharedItems,
                    expectedAssignedItems);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("팀 보따리 물품을 조회할 때, 유효하지 않은 ssaid라면 예외를 던진다.")
        @Test
        void getTeamItemStatus_Exception_InvalidSsaid() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final String invalid_ssaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(
                    () -> teamItemFacade.getTeamItemStatus(teamBottari.getId(), invalid_ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("팀 보따리 물품을 조회할 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void getTeamItemStatus_Exception_NotTeamMamber() {
            // given
            final Member member_in_team = MemberFixture.MEMBER.get();
            final Member member_not_in_team = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_in_team);
            entityManager.persist(member_not_in_team);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_in_team);
            entityManager.persist(teamBottari);

            // when & then
            assertThatThrownBy(
                    () -> teamItemFacade.getTeamItemStatus(teamBottari.getId(), member_not_in_team.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }

        @DisplayName("팀 보따리 물품 현황을 조회할 때, 팀 보따리가 존재하지 않는다면 예외를 던진다.")
        @Test
        void getTeamItemStatus_Exception_NotFoundTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalid_teamBottariId = -1L;

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getTeamItemStatus(invalid_teamBottariId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }
    }

    @Nested
    class GetCheckListTest {

        @DisplayName("체크리스트를 조회한다.")
        @Test
        void getCheckList() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
            entityManager.persist(teamSharedItem);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
            entityManager.persist(teamAssignedItem);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            // when
            final TeamMemberChecklistResponse actual = teamItemFacade.getCheckList(teamBottari.getId(),
                    member.getSsaid());

            // then
            final List<String> sharedItemNames = actual.sharedItems()
                    .stream()
                    .map(TeamMemberItemResponse::name)
                    .toList();
            final List<String> assignedItemNames = actual.assignedItems()
                    .stream()
                    .map(TeamMemberItemResponse::name)
                    .toList();
            final List<String> personalItemNames = actual.personalItems()
                    .stream()
                    .map(TeamMemberItemResponse::name)
                    .toList();
            assertAll(
                    () -> assertThat(sharedItemNames).containsExactly("공통 물품"),
                    () -> assertThat(assignedItemNames).containsExactly("담당 물품"),
                    () -> assertThat(personalItemNames).containsExactly("개인 물품")
            );
        }

        @DisplayName("체크리스트를 조회할 때, 등록되지 않은 SSAID라면 예외를 던진다.")
        @Test
        void getCheckList_Exception_InvalidSsaid() {
            // given
            final String invalid_ssaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getCheckList(1L, invalid_ssaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("체크리스트를 조회할 때, 팀에 참여하지 않은 사용자라면 예외를 던진다.")
        @Test
        void getCheckList_Exception_NotParticipantInTeam() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getCheckList(teamBottari.getId(), member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class CheckTest {

        @DisplayName("팀 아이템 타입별로 체크한다.")
        @ParameterizedTest
        @EnumSource(TeamItemType.class)
        void check(final TeamItemType type) {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long itemId = createItemByType(type, teamBottari, teamMember);
            final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

            // when
            teamItemFacade.check(itemId, member.getSsaid(), request);

            // then
            final boolean actual = getItemCheckedStatus(type, itemId);
            assertThat(actual).isTrue();
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("팀 아이템 타입별로 체크 해제한다.")
        @ParameterizedTest
        @EnumSource(TeamItemType.class)
        void uncheck(final TeamItemType type) {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long itemId = createCheckedItemByType(type, teamBottari, teamMember);

            final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

            // when
            teamItemFacade.uncheck(itemId, member.getSsaid(), request);

            // then
            final boolean actual = getItemCheckedStatus(type, itemId);
            assertThat(actual).isFalse();
        }

        private Long createCheckedItemByType(
                final TeamItemType type,
                final TeamBottari teamBottari,
                final TeamMember teamMember
        ) {
            return switch (type) {
                case SHARED -> {
                    final TeamSharedItemInfo itemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
                    entityManager.persist(itemInfo);
                    final TeamSharedItem item = new TeamSharedItem(itemInfo, teamMember);
                    item.check();
                    entityManager.persist(item);
                    yield item.getId();
                }
                case ASSIGNED -> {
                    final TeamAssignedItemInfo itemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
                    entityManager.persist(itemInfo);
                    final TeamAssignedItem item = new TeamAssignedItem(itemInfo, teamMember);
                    item.check();
                    entityManager.persist(item);
                    yield item.getId();
                }
                case PERSONAL -> {
                    final TeamPersonalItem item = new TeamPersonalItem("개인 물품", teamMember);
                    item.check();
                    entityManager.persist(item);
                    yield item.getId();
                }
            };
        }
    }

    @Nested
    class SendRemindAlarmByInfoTest {

        @DisplayName("공유 물품에 대해 보채기 알람을 전송한다.")
        @Test
        void sendRemindAlarmByInfo_Shared() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamSharedItemInfo info = new TeamSharedItemInfo("공유 물품", teamBottari);
            entityManager.persist(info);

            final TeamSharedItem item = new TeamSharedItem(info, teamMember);
            entityManager.persist(item);

            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.SHARED);

            // when & then
            assertThatCode(() -> teamItemFacade.sendRemindAlarmByInfo(info.getId(), request, member.getSsaid()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("담당 물품에 대해 보채기 알람을 전송한다.")
        @Test
        void sendRemindAlarmByInfo_Assigned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamAssignedItemInfo info = new TeamAssignedItemInfo("담당 물품", teamBottari);
            entityManager.persist(info);

            final TeamAssignedItem item = new TeamAssignedItem(info, teamMember);
            entityManager.persist(item);

            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.ASSIGNED);

            // when & then
            assertThatCode(() -> teamItemFacade.sendRemindAlarmByInfo(info.getId(), request, member.getSsaid()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("개인 물품에 대해 보채기 알람을 요청하면 예외를 던진다.")
        @Test
        void sendRemindAlarmByInfo_Personal_Exception() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem item = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(item);

            final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.PERSONAL);

            // when & then
            assertThatThrownBy(() -> teamItemFacade.sendRemindAlarmByInfo(item.getId(), request, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("적절하지 않은 아이템 타입입니다. - 보채기 알람은 공통/담당 물품만 가능합니다.");
        }
    }

    private boolean getItemCheckedStatus(
            final TeamItemType type,
            final Long itemId
    ) {
        return switch (type) {
            case SHARED -> entityManager.find(TeamSharedItem.class, itemId).isChecked();
            case ASSIGNED -> entityManager.find(TeamAssignedItem.class, itemId).isChecked();
            case PERSONAL -> entityManager.find(TeamPersonalItem.class, itemId).isChecked();
        };
    }
}
