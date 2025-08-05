package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("SortProperty 테스트")
class SortPropertyTest {

    @DisplayName("텍스트를 통해 정렬 기준을 반환한다.")
    @ParameterizedTest
    @CsvSource({
            "createdAt, CREATED_AT",
            "takenCount, TAKEN_COUNT"
    })
    void fromProperty(
            final String property,
            final SortProperty expected
    ) {
        // when
        final SortProperty actual = SortProperty.fromProperty(property);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 정렬 기준으로 변환하려 할 시, 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"invalidProperty", "date", "count"})
    void fromProperty_Exception_WhenInvalidProperty(final String property) {
        // when & then
        assertThatThrownBy(() -> SortProperty.fromProperty(property))
                .isInstanceOf(BusinessException.class)
                .hasMessage("유효하지 않은 보따리 템플릿 정렬 타입입니다.");
    }

    @DisplayName("property가 동일한지 판별한다.")
    @ParameterizedTest
    @CsvSource({
            "createdAt, CREATED_AT, true",
            "takenCount, CREATED_AT, false",
            "takenCount, TAKEN_COUNT, true",
            "createdAt, TAKEN_COUNT, false",
            "invalidProperty, TAKEN_COUNT, false"
    })
    void equalsProperty(
            final String property,
            final SortProperty sortProperty,
            final boolean expected
    ) {
        // when
        final boolean actual = sortProperty.equalsProperty(property);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
