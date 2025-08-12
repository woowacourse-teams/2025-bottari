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
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse.TeamMemberItemResponse;
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
        TeamItemService.class,
        TeamSharedItemService.class,
        TeamAssignedItemService.class,
        TeamPersonalItemService.class,
})
public class TeamItemServiceTest {

    @Autowired
    private TeamItemService teamItemService;

    @Autowired
    private EntityManager entityManager;

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
            final TeamMemberChecklistResponse actual = teamItemService.getCheckList(teamBottari.getId(),
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
            assertThatThrownBy(() -> teamItemService.getCheckList(1L, invalid_ssaid))
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
            assertThatThrownBy(() -> teamItemService.getCheckList(teamBottari.getId(), member.getSsaid()))
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
            teamItemService.check(itemId, member.getSsaid(), request);

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
            teamItemService.uncheck(itemId, member.getSsaid(), request);

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
