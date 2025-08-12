package com.bottari.bottaritemplate.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTemplateItemTest {

    @DisplayName("보따리 템플릿 물품명이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void validateName_Blank(final String name) {
        // given
        final Member member = new Member("ssaid", "name");
        final BottariTemplate bottariTemplate = new BottariTemplate("title", member);

        // when & then
        assertThatThrownBy(() -> new BottariTemplateItem(name, bottariTemplate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 템플릿 물품명은 공백일 수 없습니다.");
    }

    @DisplayName("보따리 템플릿 물품명이 20자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"이름이너무길어서템플릿물품명으로사용못해요", "이름이너무길어서보따리템플릿물품명으로사용할수없습니다!"})
    void validateName_TooLong(final String name) {
        // given
        final Member member = new Member("ssaid", "name");
        final BottariTemplate bottariTemplate = new BottariTemplate("title", member);

        // when & then
        assertThatThrownBy(() -> new BottariTemplateItem(name, bottariTemplate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 템플릿 물품명이 너무 깁니다. - 최대 20자까지 입력 가능합니다.");
    }
}
