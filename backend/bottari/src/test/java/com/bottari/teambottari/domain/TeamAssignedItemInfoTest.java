package com.bottari.teambottari.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeamAssignedItemInfoTest {

    @DisplayName("팀 보따리 물품명이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void validateName_Blank(final String name) {
        // given
        final Member member = MemberFixture.MEMBER.get();
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);

        // when & then
        assertThatThrownBy(() -> new TeamAssignedItemInfo(name, teamBottari))
                .isInstanceOf(BusinessException.class)
                .hasMessage("팀 보따리 물품명은 공백일 수 없습니다.");
    }

    @DisplayName("팀 보따리 물품명이 20자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"이름이너무길어서보따리물품명으로사용할수없습니다", "이름이너무길어서보따리물품명으로사용할수없습니다!"})
    void validateName_TooLong(final String name) {
        // given
        final Member member = MemberFixture.MEMBER.get();
        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);

        // when & then
        assertThatThrownBy(() -> new TeamAssignedItemInfo(name, teamBottari))
                .isInstanceOf(BusinessException.class)
                .hasMessage("팀 보따리 물품명이 너무 깁니다. - 최대 20자까지 입력 가능합니다.");
    }
}
