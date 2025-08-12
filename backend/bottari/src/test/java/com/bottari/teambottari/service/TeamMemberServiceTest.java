package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TeamMemberService.class)
class TeamMemberServiceTest {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private EntityManager entityManager;


    @Nested
    class GetTeamMemberInfoByTeamBottariIdTest {

        @DisplayName("팀원 정보 및 팀원 초대 코드를 조회한다.")
        @Test
        void getTeamMemberInfoByTeamBottariId() {
            // given
            final Member owner = MemberFixture.MEMBER.get();
            entityManager.persist(owner);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(owner);
            entityManager.persist(teamBottari);

            final TeamMember ownerTeamMember = new TeamMember(teamBottari, owner);
            entityManager.persist(ownerTeamMember);

            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(anotherTeamMember);

            // when
            final ReadTeamMemberInfoResponse actual = teamMemberService.getTeamMemberInfoByTeamBottariId(
                    teamBottari.getId(), anotherMember.getSsaid()
            );

            // then
            assertAll(
                    () -> assertThat(actual.inviteCode()).isEqualTo("inviteCode"),
                    () -> assertThat(actual.teamMemberCount()).isEqualTo(2),
                    () -> assertThat(actual.ownerName()).isEqualTo(owner.getName()),
                    () -> assertThat(actual.teamMemberNames()).containsExactlyInAnyOrder(
                            owner.getName(),
                            anotherMember.getName()
                    )
            );
        }

        @DisplayName("존재하지 않는 팀 보따리일 경우, 예외를 던진다.")
        @Test
        void getTeamMemberInfoByTeamBottariId_Exception_TeamBottariNotFound() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsTeamBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberInfoByTeamBottariId(
                    notExistsTeamBottariId, member.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.TEAM_BOTTARI_NOT_FOUND.getMessage());
        }

        @DisplayName("해당 ssaid의 멤버가 팀 보따리에 속한 멤버가 아닐 경우, 예외를 던진다.")
        @Test
        void getTeamMemberInfoByTeamBottariId_Exception_MemberNotInTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberInfoByTeamBottariId(
                    teamBottari.getId(), anotherMember.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI.getMessage());
        }
    }
}
