package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("이름이 3글자 미만 10글자 초과인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"12", "12345678910"})
    void validateName(final String name) {
        // when & then
        assertThatThrownBy(() -> new Member("ssaid", name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 이름은 3글자 이상 10글자 이하여야 합니다.");
    }

    @DisplayName("같은 ssaid인지 비교한다.")
    @ParameterizedTest
    @CsvSource({
            "same_ssaid, true",
            "diff_ssaid, false"
    })
    void isSameBySsaid(
            final String ssaid,
            final boolean expected
    ) {
        // given
        final Member member = new Member("same_ssaid", "name");

        // when
        final boolean actual = member.isSameBySsaid(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
