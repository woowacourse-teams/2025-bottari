package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTemplateItemTest {

    @DisplayName("보따리 템플릿 물품명이 공백이거나 20자 초과인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "스무 글자가 넘는 템플릿 물품명입니다."})
    void validateName(final String name) {
        // given
        final Member member = new Member("ssaid", "name");
        final BottariTemplate bottariTemplate = new BottariTemplate("title", member);

        // when & then
        assertThatThrownBy(() -> new BottariTemplateItem(name, bottariTemplate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보따리 템플릿 물품명은 공백이거나 20자를 넘을 수 없습니다.");
    }
}
