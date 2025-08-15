package com.bottari.teambottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class TeamSharedItemInfoTest {

    @DisplayName("팀 공유 보따리 물품을 체크한다.")
    @Test
    void check() {
        // given
        final Member member = new Member("ssaid", "name");
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(member.getName(), teamBottari);
        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);

        // when
        teamSharedItem.check();

        // then
        assertThat(teamSharedItem.isChecked()).isTrue();
    }

    @DisplayName("이미 체크 되어있는 팀 공유 보따리 물품을 체크하면, 예외를 던진다.")
    @Test
    void check_Exception_AlreadyCheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(member.getName(), teamBottari);
        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
        teamSharedItem.check();

        // when & then
        assertThatThrownBy(teamSharedItem::check)
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 팀 보따리 물품은 이미 체크되어 있습니다. - 공통");
    }

    @DisplayName("팀 공유 보따리 물품을 체크 해제한다.")
    @Test
    void uncheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(member.getName(), teamBottari);
        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
        teamSharedItem.check();

        // when
        teamSharedItem.uncheck();

        // then
        assertThat(teamSharedItem.isChecked()).isFalse();
    }

    @DisplayName("이미 체크 해제되어있는 팀 공유 보따리 물품을 체크 해제하면, 예외를 던진다.")
    @Test
    void uncheck_Exception_AlreadyUncheck() {
        // given
        final Member member = new Member("ssaid", "name");
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(member.getName(), teamBottari);
        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);

        // when & then
        assertThatThrownBy(teamSharedItem::uncheck)
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 팀 보따리 물품은 이미 체크 해제되어 있습니다. - 공통");
    }

    @DisplayName("본인의 팀 공유 보따리 물품인지 확인한다.")
    @ParameterizedTest
    @CsvSource({
            "same_ssaid, true",
            "diff_ssaid, false"
    })
    void isOwner(
            final String ssaid,
            final boolean expected
    ) {
        // given
        final Member member = new Member("same_ssaid", "name");
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(member.getName(), teamBottari);
        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);

        // when
        final boolean actual = teamSharedItem.isOwner(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
