package com.bottari.teambottari.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamMemberTest {

    @DisplayName("해당 팀 멤버가 팀 보따리 주인이라면 true를 반환한다.")
    @Test
    void isTeamBottariOwner_true() {
        // given
        final Member member = MemberFixture.MEMBER.get();

        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);

        // when
        final boolean actual = teamMember.isTeamBottariOwner();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("해당 팀 멤버가 팀 보따리 주인이 아니라면 false를 반환한다.")
    @Test
    void isTeamBottariOwner_false() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();

        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);

        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);

        // when
        final boolean actual = anotherTeamMember.isTeamBottariOwner();

        // then
        assertThat(actual).isFalse();
    }
}
