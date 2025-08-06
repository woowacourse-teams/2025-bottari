package com.bottari.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("이름이 3글자 미만인 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "12"})
    void validateName_TooShort(final String name) {
        // when & then
        assertThatThrownBy(() -> new Member("ssaid", name))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자 이름이 너무 짧습니다. - 최소 3자 이상 입력 가능합니다.");
    }

    @DisplayName("이름이 10글자를 초과하는 경우, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"12345678901", "123456789012"})
    void validateName_TooLong(final String name) {
        // when & then
        assertThatThrownBy(() -> new Member("ssaid", name))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자 이름이 너무 깁니다. - 최대 10자까지 입력 가능합니다.");
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

    @DisplayName("이름을 수정한다.")
    @Test
    void updateName() {
        // given
        final Member member = new Member("ssaid", "name");

        // when
        member.updateName("new_name");

        // then
        assertThat(member.getName()).isEqualTo("new_name");
    }

    @DisplayName("기존 이름과 동일한 이름으로는 수정할 수 없다.")
    @Test
    void updateName_Exception_SameName() {
        // given
        final Member member = new Member("ssaid", "name");

        // when & then
        assertThatThrownBy(() -> member.updateName("name"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("기존의 사용자 이름과 동일한 이름으로는 변경할 수 없습니다.");
    }
}
