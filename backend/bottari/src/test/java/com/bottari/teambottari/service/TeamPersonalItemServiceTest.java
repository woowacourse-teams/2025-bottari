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
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.dto.CreatePersonalItemRequest;
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
@Import(TeamPersonalItemService.class)
class TeamPersonalItemServiceTest {

    @Autowired
    private TeamPersonalItemService teamPersonalItemService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리 개인 물품을 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "개인 물품";
            final CreatePersonalItemRequest request = new CreatePersonalItemRequest(itemName);

            // when
            final Long actual = teamPersonalItemService.create(teamMember, request);

            // then
            final TeamPersonalItem actualItem = entityManager.find(TeamPersonalItem.class, actual);
            assertAll(
                    () -> assertThat(actualItem).isNotNull(),
                    () -> assertThat(actualItem.getName()).isEqualTo(itemName),
                    () -> assertThat(actualItem.getTeamMember()).isEqualTo(teamMember)
            );
        }

        @DisplayName("팀 보따리 개인 물품을 생성 시, 이미 존재하는 물품 이름이라면, 예외를 던진다.")
        @Test
        void create_Exception_AlreadyExistsName() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final String itemName = "개인 물품";
            entityManager.persist(new TeamPersonalItem(itemName, teamMember));

            final CreatePersonalItemRequest request = new CreatePersonalItemRequest(itemName);

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.create(teamMember, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 존재하는 팀 보따리 물품입니다. - 개인");
        }
    }

    @Nested
    class Delete {

        @DisplayName("팀 보따리 개인 물품을 삭제한다.")
        @Test
        void delete() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            // when
            teamPersonalItemService.delete(teamPersonalItem.getId(), member.getSsaid());

            // then
            final TeamPersonalItem actual = entityManager.find(TeamPersonalItem.class, teamPersonalItem.getId());
            assertThat(actual).isNull();
        }

        @DisplayName("팀 보따리 개인 물품 삭제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void delete_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.delete(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 개인");
        }

        @DisplayName("팀 보따리 개인 물품을 삭제 시, 주인이 아니라면, 예외를 던진다.")
        @Test
        void delete_Exception_NotOwned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.delete(teamPersonalItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
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

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            // when
            final List<TeamMemberItemResponse> actual = teamPersonalItemService.getAllByTeamMember(teamMember);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.getFirst().name()).isEqualTo(teamPersonalItem.getName())
            );
        }
    }

    @Nested
    class CheckTest {

        @DisplayName("팀 보따리 개인 물품을 체크한다.")
        @Test
        void check() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            // when
            teamPersonalItemService.check(teamPersonalItem.getId(), member.getSsaid());

            // then
            final TeamPersonalItem actual = entityManager.find(TeamPersonalItem.class, teamPersonalItem.getId());
            assertThat(actual.isChecked()).isTrue();
        }

        @DisplayName("팀 보따리 개인 물품을 체크 시, 주인이 아니라면, 예외를 던진다.")
        @Test
        void check_Exception_NotOwned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.check(teamPersonalItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 개인 물품 체크 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void check_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.check(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 개인");
        }
    }

    @Nested
    class UncheckTest {

        @DisplayName("팀 보따리 개인 물품을 체크 해제한다.")
        @Test
        void uncheck() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            teamPersonalItem.check();
            entityManager.persist(teamPersonalItem);

            // when
            teamPersonalItemService.uncheck(teamPersonalItem.getId(), member.getSsaid());

            // then
            final TeamPersonalItem actual = entityManager.find(TeamPersonalItem.class, teamPersonalItem.getId());
            assertThat(actual.isChecked()).isFalse();
        }

        @DisplayName("팀 보따리 개인 물품 체크 해제 시, 물품 주인이 아니라면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotOwned() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
            entityManager.persist(teamPersonalItem);

            final String invalidSsaid = "invalid_ssaid";

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.uncheck(teamPersonalItem.getId(), invalidSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다. - 본인의 팀 보따리 물품이 아닙니다.");
        }

        @DisplayName("팀 보따리 개인 물품 체크 해제 시, 물품을 찾을 수 없다면, 예외를 던진다.")
        @Test
        void uncheck_Exception_NotExistsItem() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsBottariItemId = 1L;

            // when & then
            assertThatThrownBy(() -> teamPersonalItemService.uncheck(notExistsBottariItemId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리 물품을 찾을 수 없습니다. - 개인");
        }
    }
}
