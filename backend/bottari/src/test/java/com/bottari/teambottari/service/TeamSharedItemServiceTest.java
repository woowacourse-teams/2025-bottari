package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
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
@Import(TeamSharedItemService.class)
class TeamSharedItemServiceTest {

    @Autowired
    private TeamSharedItemService teamSharedItemService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리 공통 물품을 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member anotherMember = MemberFixture.MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);
            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(anotherTeamMember);

            final String itemName = "공통 물품";
            final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);

            // when
            final Long actual = teamSharedItemService.create(teamMember, request);

            // then
            final TeamSharedItemInfo teamSharedItemInfo = entityManager.find(TeamSharedItemInfo.class, actual);
            final List<TeamSharedItem> teamSharedItems = entityManager.createQuery(
                            "SELECT tsi FROM TeamSharedItem tsi WHERE tsi.info.id = :infoId", TeamSharedItem.class)
                    .setParameter("infoId", actual)
                    .getResultList();
            assertAll(
                    () -> assertThat(teamSharedItemInfo).isNotNull(),
                    () -> assertThat(teamSharedItemInfo.getName()).isEqualTo(itemName),
                    () -> assertThat(teamSharedItems).hasSize(2),
                    () -> assertThat(teamSharedItems.getFirst().getInfo()).isEqualTo(teamSharedItemInfo),
                    () -> assertThat(teamSharedItems.getFirst().getTeamMember()).isEqualTo(teamMember),
                    () -> assertThat(teamSharedItems.getLast().getTeamMember()).isEqualTo(anotherTeamMember)
            );
        }

        @DisplayName("팀 보따리 공통 물품을 생성할 때, 중복된 이름이 있다면 예외를 던진다.")
        @Test
        void create_Exception_DuplicateName() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "공통 물품";
            final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);

            // when
            teamSharedItemService.create(teamMember, request);

            // then
            assertThatThrownBy(() -> teamSharedItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 존재하는 팀 보따리 물품입니다. - 공통");
        }
    }

    @Nested
    class DeleteTest {

        @DisplayName("팀 보따리 공통 물품을 삭제한다.")
        @Test
        void delete() {
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

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
            entityManager.persist(teamSharedItem);
            final TeamSharedItem anotherTeamSharedItem = new TeamSharedItem(teamSharedItemInfo, anotherTeamMember);
            entityManager.persist(anotherTeamSharedItem);

            entityManager.flush();
            entityManager.clear();

            // when
            teamSharedItemService.delete(teamSharedItemInfo.getId(), member.getSsaid());

            // then
            final TeamSharedItemInfo actual = entityManager.find(TeamSharedItemInfo.class, teamSharedItemInfo.getId());
            final List<TeamSharedItem> existingItems = entityManager.createQuery(
                            "SELECT tsi FROM TeamSharedItem tsi WHERE tsi.info.id = :infoId", TeamSharedItem.class)
                    .setParameter("infoId", teamSharedItemInfo.getId())
                    .getResultList();
            assertAll(
                    () -> assertThat(actual).isNull(),
                    () -> assertThat(existingItems).hasSize(0)
            );
        }

        @DisplayName("팀 보따리 공통 물품 삭제 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void delete_Exception_NotOwned() {
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

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.delete(teamSharedItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 공통");
        }

        @DisplayName("팀 보따리 공통 물품 삭제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void delete_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.delete(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 공통");
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

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
            entityManager.persist(teamSharedItem);

            // when
            final List<TeamMemberItemResponse> actual = teamSharedItemService.getAllByTeamMember(teamMember);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.getFirst().name()).isEqualTo(teamSharedItemInfo.getName())
            );
        }
    }

    @Nested
    class CheckTest {

        @DisplayName("팀 보따리 공통 물품을 체크한다.")
        @Test
        void check() {
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

            // when
            teamSharedItemService.check(teamSharedItem.getId(), member.getSsaid());

            // then
            final TeamSharedItem actual = entityManager.find(TeamSharedItem.class, teamSharedItem.getId());
            assertThat(actual.isChecked()).isTrue();
        }

        @DisplayName("팀 보따리 공통 물품 체크 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void check_Exception_NotOwned() {
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

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.check(teamSharedItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 공통 물품 체크 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void check_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.check(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 공통");
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("팀 보따리 공통 물품을 체크 해제한다.")
        @Test
        void uncheck() {
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
            teamSharedItem.check();
            entityManager.persist(teamSharedItem);

            // when
            teamSharedItemService.uncheck(teamSharedItem.getId(), member.getSsaid());

            // then
            final TeamSharedItem actual = entityManager.find(TeamSharedItem.class, teamSharedItem.getId());
            assertThat(actual.isChecked()).isFalse();
        }

        @DisplayName("팀 보따리 공통 물품 체크 해제 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotOwned() {
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

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.uncheck(teamSharedItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 공통 물품 체크 해제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamSharedItemService.uncheck(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 공통");
        }
    }
}
