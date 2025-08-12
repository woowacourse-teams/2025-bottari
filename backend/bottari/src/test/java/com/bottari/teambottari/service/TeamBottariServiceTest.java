package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TeamBottariService.class, JpaAuditingConfig.class})
class TeamBottariServiceTest {

    @Autowired
    private TeamBottariService teamBottariService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리를 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            assertThat(teamBottariId).isNotNull();
        }

        @DisplayName("팀 보따리 생성 시, 팀 보따리의 초대 코드가 생성된다.")
        @Test
        void create_GeneratesInviteCode() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final String inviteCode = entityManager.find(TeamBottari.class, teamBottariId)
                    .getInviteCode();
            assertThat(inviteCode).isNotBlank();
        }

        @DisplayName("팀 보따리 생성 시, 팀 보따리의 소유자가 올바르게 설정된다.")
        @Test
        void create_SetsOwnerCorrectly() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final TeamBottari teamBottari = entityManager.find(TeamBottari.class, teamBottariId);
            assertThat(teamBottari.getOwner()).isEqualTo(member);
        }

        @DisplayName("팀 보따리 생성 시, 팀 멤버가 올바르게 생성된다.")
        @Test
        void create_CreatesTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final TeamBottari teamBottari = entityManager.find(TeamBottari.class, teamBottariId);
            final Long teamMemberCount = entityManager.createQuery(
                    "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.teamBottari = :teamBottari", Long.class)
                    .setParameter("teamBottari", teamBottari)
                    .getSingleResult();
            assertThat(teamMemberCount).isEqualTo(1);
        }

        @DisplayName("존재하지 않는 사용자로 팀 보따리를 생성할 경우, 예외를 던진다.")
        @Test
        void create_Exception_WhenNonExistentUser() {
            // given
            final String nonExistentSsaid = "non-existent-ssaid";
            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when & then
            assertThatThrownBy(() -> teamBottariService.create(nonExistentSsaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }
}
