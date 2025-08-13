package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
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
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.ReadTeamItemsResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        TeamItemFacade.class,
        TeamSharedItemService.class,
        TeamAssignedItemService.class,
        TeamPersonalItemService.class,
})
public class TeamItemFacadeTest {

    @Autowired
    private TeamItemFacade teamItemFacade;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class GetTeamItemsTest {

        @DisplayName("팀 멤버가 아니라면, 팀 보따리 물품 현황을 확인하 수 없다.")
        @Test
        void getTeamItems() {
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
            final ReadTeamItemsResponse actual = teamItemFacade.getTeamItems(teamBottari.getId(),
                    member_1.getSsaid());

            // then
            final TeamItemStatusResponse actualSharedItemResponse = actual.sharedItems().getFirst();
            final TeamItemStatusResponse member_1_assignedItemResponse = actual.assignedItems()
                    .stream()
                    .filter(response -> response.name().equals("멤버 1 물품"))
                    .findFirst()
                    .orElse(null);
            final TeamItemStatusResponse member_2_assignedItemResponse = actual.assignedItems()
                    .stream()
                    .filter(response -> response.name().equals("멤버 2 물품"))
                    .findFirst()
                    .orElse(null);
            assertAll(
                    () -> assertThat(actual.sharedItems()).hasSize(1),
                    () -> assertThat(actual.assignedItems()).hasSize(2),
                    () -> assertThat(actualSharedItemResponse.checkCount()).isEqualTo(0),
                    () -> assertThat(actualSharedItemResponse.totalCount()).isEqualTo(2),
                    () -> assertThat(member_1_assignedItemResponse.name()).isEqualTo(teamAssignedItemInfo_1.getName()),
                    () -> assertThat(member_1_assignedItemResponse.checkCount()).isEqualTo(0),
                    () -> assertThat(member_1_assignedItemResponse.totalCount()).isEqualTo(1),
                    () -> assertThat(member_2_assignedItemResponse.name()).isEqualTo(teamAssignedItemInfo_2.getName()),
                    () -> assertThat(member_2_assignedItemResponse.checkCount()).isEqualTo(1),
                    () -> assertThat(member_2_assignedItemResponse.totalCount()).isEqualTo(1)
            );
        }

        @DisplayName("팀 보따리 물품을 조회할 때, 팀 멤버가 아니라면 예외를 던진다.")
        @Test
        void getTeamItems_Exception_NotTeamMamber() {
            // given
            final Member member_in_team = MemberFixture.MEMBER.get();
            final Member member_not_in_team = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(member_in_team);
            entityManager.persist(member_not_in_team);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member_in_team);
            entityManager.persist(teamBottari);

            // when & then
            assertThatThrownBy(() -> teamItemFacade.getTeamItems(teamBottari.getId(), member_not_in_team.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
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
        @CsvSource({
                "SHARED",
                "ASSIGNED",
                "PERSONAL"
        })
        void check(final TeamItemType type) {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long itemId = createItemByType(type, teamBottari, teamMember);
            final CheckTeamItemRequest request = new CheckTeamItemRequest(type);

            // when
            teamItemFacade.check(itemId, member.getSsaid(), request);

            // then
            final boolean actual = getItemCheckedStatus(type, itemId);
            assertThat(actual).isTrue();
        }

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
                    yield item.getId();
                }
                case ASSIGNED -> {
                    final TeamAssignedItemInfo itemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
                    entityManager.persist(itemInfo);
                    final TeamAssignedItem item = new TeamAssignedItem(itemInfo, teamMember);
                    entityManager.persist(item);
                    yield item.getId();
                }
                case PERSONAL -> {
                    final TeamPersonalItem item = new TeamPersonalItem("개인 물품", teamMember);
                    entityManager.persist(item);
                    yield item.getId();
                }
            };
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("팀 아이템 타입별로 체크 해제한다.")
        @ParameterizedTest
        @CsvSource({
                "SHARED",
                "ASSIGNED",
                "PERSONAL"
        })
        void uncheck(final TeamItemType type) {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Long itemId = createCheckedItemByType(type, teamBottari, teamMember);

            final CheckTeamItemRequest request = new CheckTeamItemRequest(type);

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
