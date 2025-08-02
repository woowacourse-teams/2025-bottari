package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTemplateTest {

    @DisplayName("보따리 템플릿 이름이 공백이거나 15자 초과인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "열다섯글자가넘는템플릿이름입니다"})
    void validateTitle(final String title) {
        // given
        final Member member = new Member("ssaid", "name");

        // when & then
        assertThatThrownBy(() -> new BottariTemplate(title, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리 템플릿 이름은 공백이거나 15자를 넘을 수 없습니다.");
    }

    @DisplayName("본인의 보따리 템플릿인지 확인한다.")
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
        final BottariTemplate bottariTemplate = new BottariTemplate("title",member);

        // when
        final boolean actual = bottariTemplate.isOwner(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
