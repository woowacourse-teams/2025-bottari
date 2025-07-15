package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
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
}
