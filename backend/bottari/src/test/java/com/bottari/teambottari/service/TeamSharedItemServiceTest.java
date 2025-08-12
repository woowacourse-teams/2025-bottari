package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import jakarta.persistence.EntityManager;
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
    class CheckTest {

        @DisplayName("보따리 물품을 체크한다.")
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

        @DisplayName("물품 체크 시, 보따리 물품 주인이 아니라면, 예외를 던진다.")
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

        @DisplayName("물품 체크 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
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

        @DisplayName("보따리 물품을 체크 해제한다.")
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

        @DisplayName("물품 체크 해제 시, 보따리 물품 주인이 아니라면, 예외를 던진다.")
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

        @DisplayName("물품 체크 해제 시, 보따리 물품을 찾을 수 없다면, 예외를 던진다.")
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
