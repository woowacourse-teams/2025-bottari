package com.bottari.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTitleTest {

    @DisplayName("보따리 제목이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    void validateTitle_Blank(final String title) {
        // when & then
        assertThatThrownBy(() -> new BottariTitle(title))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목은 공백일 수 없습니다.");
    }

    @DisplayName("보따리 제목이 15자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"열다섯글자가넘는보따리이름입니다", "열다섯글자가넘는보따리이름입니다!"})
    void validateTitle_TooLong(final String title) {
        // when & then
        assertThatThrownBy(() -> new BottariTitle(title))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목이 너무 깁니다. - 최대 15자까지 입력 가능합니다.");
    }

    @DisplayName("보따리 제목에 욕설이 들어가있는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"씨발이네", "씨@발 안녕하세요", "병1신이세요?", "ㅅㅂ입니다."})
    void validateTitle_BadWord(final String title) {
        // when & then
        assertThatThrownBy(() -> new BottariTitle(title))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목에 욕설을 입력할 수 없습니다.");
    }
}
