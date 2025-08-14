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

@DataJpaTest
@Import(TeamAssignedItemService.class)
class TeamAssignedItemServiceTest {

    @Autowired
    private TeamAssignedItemService teamAssignedItemService;

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
                    List.of(member.getName())
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
                    "SELECT tai FROM TeamAssignedItem tai WHERE tai.teamMember.id = :teamMemberId", TeamAssignedItem.class)
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
                    List.of(member.getName())
            );

            // when & then
            assertThatThrownBy(() -> teamAssignedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 존재하는 팀 보따리 물품입니다. - 담당");
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
}
