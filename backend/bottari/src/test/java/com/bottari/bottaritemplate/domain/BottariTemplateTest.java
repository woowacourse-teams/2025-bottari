package com.bottari.bottaritemplate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTemplateTest {

    @DisplayName("보따리 템플릿 이름이 공백인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void validateTitle_Blank(final String title) {
        // given
        final Member member = new Member("ssaid", "name");

        // when & then
        assertThatThrownBy(() -> new BottariTemplate(title, member))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 템플릿 제목은 공백일 수 없습니다.");
    }

    @DisplayName("보따리 템플릿 이름이 15자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"열다섯글자가넘는보따리템플릿이름입니다", "열다섯글자가넘는보따리템플릿이름입니다!"})
    void validateTitle_TooLong(final String title) {
        // given
        final Member member = new Member("ssaid", "name");

        // when & then
        assertThatThrownBy(() -> new BottariTemplate(title, member))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 템플릿 제목이 너무 깁니다. - 최대 15자까지 입력 가능합니다.");
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
        final BottariTemplate bottariTemplate = new BottariTemplate("title", member);

        // when
        final boolean actual = bottariTemplate.isOwner(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
