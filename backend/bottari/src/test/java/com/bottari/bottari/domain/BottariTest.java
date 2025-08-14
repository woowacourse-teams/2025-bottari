package com.bottari.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BottariTest {

    @DisplayName("본인의 보따리인지 확인한다.")
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
        final Bottari bottari = new Bottari("title", member);

        // when
        final boolean actual = bottari.isOwner(ssaid);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보따리 제목을 수정한다.")
    @Test
    void updateTitle() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("original_title", member);
        final String newTitle = "updated_title";

        // when
        bottari.updateTitle(newTitle);

        // then
        assertThat(bottari.getTitle()).isEqualTo("updated_title");
    }

    @DisplayName("기존 제목과 동일한 제목으로 수정할 경우, 예외를 던진다.")
    @Test
    void updateTitle_Exception_SameTitle() {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);

        // when & then
        assertThatThrownBy(() -> bottari.updateTitle("title"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("기존의 보따리 이름과 동일한 이름으로는 변경할 수 없습니다.");
    }

    @DisplayName("공백인 제목으로 수정할 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void updateTitle_Exception_Blank(final String invalidTitle) {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);

        // when & then
        assertThatThrownBy(() -> bottari.updateTitle(invalidTitle))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목은 공백일 수 없습니다.");
    }

    @DisplayName("15자가 넘는 제목으로 수정할 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"열다섯글자가넘는보따리이름입니다", "열다섯글자가넘는보따리이름입니다!"})
    void updateTitle_Exception_TooLongTitle(final String tooLongTitle) {
        // given
        final Member member = new Member("ssaid", "name");
        final Bottari bottari = new Bottari("title", member);

        // when & then
        assertThatThrownBy(() -> bottari.updateTitle(tooLongTitle))
                .isInstanceOf(BusinessException.class)
                .hasMessage("보따리 제목이 너무 깁니다. 최대 15자까지 입력 가능합니다.");
    }
}
