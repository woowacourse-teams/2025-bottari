package com.bottari.teambottari.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeamBottariTest {

    @DisplayName("팀 보따리 제목이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    void validateTitle_Blank(final String title) {
        // given
        final Member member = MemberFixture.MEMBER.get();

        // when & then
        assertThatThrownBy(() -> new TeamBottari(title, member, "inviteCode"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목은 공백일 수 없습니다.");
    }

    @DisplayName("팀 보따리 제목이 15자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"열다섯글자가넘는보따리이름입니다", "열다섯글자가넘는보따리이름입니다!"})
    void validateTitle_TooLong(final String title) {
        // given
        final Member member = MemberFixture.MEMBER.get();

        // when & then
        assertThatThrownBy(() -> new TeamBottari(title, member, "inviteCode"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목이 너무 깁니다. - 최대 15자까지 입력 가능합니다.");
    }
}
