package com.bottari.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ItemNameTest {

    @DisplayName("물품명이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void validateName_Blank(final String name) {
        // when & then
        assertThatThrownBy(() -> new ItemName(name))
                .isInstanceOf(BusinessException.class)
                .hasMessage("물품명은 공백일 수 없습니다.");
    }

    @DisplayName("물품명이 20자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"이름이너무길어서보따리물품명으로사용할수없습니다", "이름이너무길어서보따리물품명으로사용할수없습니다!"})
    void validateName_TooLong(final String name) {
        // when & then
        assertThatThrownBy(() -> new ItemName(name))
                .isInstanceOf(BusinessException.class)
                .hasMessage("물품명이 너무 깁니다. - 최대 20자까지 입력 가능합니다.");
    }

    @DisplayName("물풀명에 욕설이 들어가있는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"씨발이네", "씨@발 안녕하세요", "병1신이세요?", "ㅅㅂ입니다."})
    void validateName_BadWord(final String name) {
        // when & then
        assertThatThrownBy(() -> new ItemName(name))
                .isInstanceOf(BusinessException.class)
                .hasMessage("물품명에 비속어를 입력할 수 없습니다.");
    }
}
