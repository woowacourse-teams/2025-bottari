package com.bottari.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BadWordValidatorTest {

    @DisplayName("문자에 욕설이 들어간 경우, true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"씨발이네", "씨@발 안녕하세요", "병1신이세요?", "ㅅㅂ입니다."})
    void hasBadWord_WhenBadWord(final String input) {
        // when
        final boolean isBadWord = BadWordValidator.hasBadWord(input);

        // then
        assertThat(isBadWord).isTrue();
    }

    @DisplayName("문자에 욕설이 없는 경우, false를 반환한다. ")
    @ParameterizedTest
    @ValueSource(strings = {"안녕하세요 코기입니다.", "안녕하세요", "   "})
    void hasBadWord_WhenNoBadWord(final String input) {
        // when
        final boolean isBadWord = BadWordValidator.hasBadWord(input);

        // then
        assertThat(isBadWord).isFalse();
    }
}
