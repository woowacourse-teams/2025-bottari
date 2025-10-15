package com.bottari.bottaritemplate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Pageable;

class BottariTemplateHashtagCursorTest {

    @Nested
    class NormalizationTest {

        @DisplayName("hashtag id가 null인 경우 예외가 발생한다.")
        @Test
        void normalizeHashtagId_Exception_IdIsNull() {
            // when & then
            assertThatThrownBy(() -> new BottariTemplateHashtagCursor(
                    null,
                    1L,
                    "info",
                    0,
                    10,
                    "createdAt"
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그로 템플릿 검색시 해시태그 아이디는 필수입니다.");
        }

        @DisplayName("page 정규화 테스트")
        @ParameterizedTest
        @CsvSource({
                "-1, 0",
                "-100, 0",
                "0, 0",
                "1, 1",
                "100, 100"
        })
        void normalizePage(
                final int page,
                final int expected
        ) {
            // when
            final BottariTemplateHashtagCursor actual = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    "info",
                    page,
                    10,
                    "createdAt"
            );

            // then
            assertThat(actual.getPage()).isEqualTo(expected);
        }

        @DisplayName("size 정규화 테스트")
        @ParameterizedTest
        @CsvSource({
                "-1, 10",
                "0, 10",
                "1, 1",
                "50, 50"
        })
        void normalizeSize(
                final int size,
                final int expected
        ) {
            // when
            final BottariTemplateHashtagCursor actual = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    null,
                    0,
                    size,
                    "createdAt"
            );

            // then
            assertThat(actual.getSize()).isEqualTo(expected);
        }

        @DisplayName("lastId 정규화 테스트")
        @Test
        void normalizeLastId() {
            // given
            final Long nullLastId = null;

            // when
            final BottariTemplateHashtagCursor actual = new BottariTemplateHashtagCursor(
                    1L,
                    nullLastId,
                    null,
                    0,
                    10,
                    "createdAt"
            );

            // then
            assertThat(actual.getLastId()).isEqualTo(Long.MAX_VALUE);
        }

        @DisplayName("property 정규화 테스트")
        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n"})
        void normalizeProperty(final String input) {
            // given
            final String expected = "createdAt";

            // when
            final BottariTemplateHashtagCursor actual = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    "info",
                    0,
                    10,
                    input
            );

            // then
            assertThat(actual.getProperty()).isEqualTo(expected);
        }
    }

    @Nested
    class ToPageableTest {

        @DisplayName("page와 size로 Pageable 객체를 생성한다.")
        @ParameterizedTest
        @CsvSource({
                "0, 10",
                "1, 20",
                "5, 50"
        })
        void toPageable(
                final int page,
                final int size
        ) {
            // given
            final BottariTemplateHashtagCursor cursor = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    "info",
                    page,
                    size,
                    "createdAt"
            );

            // when
            final Pageable actual = cursor.toPageable();

            // then
            assertAll(
                    () -> assertThat(actual.getPageNumber()).isEqualTo(page),
                    () -> assertThat(actual.getPageSize()).isEqualTo(size)
            );
        }
    }

    @Nested
    class GetCreatedAtTest {

        @DisplayName("LocalDateTime으로 파싱한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "2024-01-01T00:00:01",
                "2023-12-31T23:59:59",
                "2024-06-15T12:30:45"
        })
        void getCreatedAt(final String dateString) {
            // given
            final BottariTemplateHashtagCursor cursor = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    dateString,
                    0,
                    10,
                    "createdAt"
            );

            // when
            final LocalDateTime actual = cursor.getCreatedAt();

            // then
            assertThat(actual.toString()).isEqualTo(dateString);
        }

        @DisplayName("잘못된 날짜 형식으로 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "2024-01-01",
                "invalid-date",
                "2024/01/01 00:00:00"
        })
        void getCreatedAt_Exception_InvalidDateTimeFormat(final String invalidDate) {
            // given
            final BottariTemplateHashtagCursor cursor = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    invalidDate,
                    0,
                    10,
                    "createdAt"
            );

            // when & then
            assertThatThrownBy(cursor::getCreatedAt)
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유효하지 않은 날짜 형식입니다. - 보따리 템플릿의 생성일자는 (yyyy-MM-dd'T'HH:mm:ss) 형식이어야 합니다.");
        }
    }

    @Nested
    class GetTakenCountTest {

        @DisplayName("유효한 숫자 문자열을 Integer으로 파싱한다.")
        @ParameterizedTest
        @ValueSource(strings = {"0", "1", "100", "2147483647"})
        void getTakenCount(final String numberString) {
            // given
            final BottariTemplateHashtagCursor cursor = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    numberString,
                    0,
                    10,
                    "takenCount"
            );

            // when
            final Integer actual = cursor.getTakenCount();

            // then
            assertThat(actual).isEqualTo(Integer.parseInt(numberString));
        }

        @DisplayName("잘못된 숫자 형식으로 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "invalid-number",
                "12.34",
                "1,000",
                "9223372036854775807" // Long.MAX_VALUE
        })
        void getTakenCount_Exception_InvalidNumberFormat(final String invalidNumber) {
            // given
            final BottariTemplateHashtagCursor cursor = new BottariTemplateHashtagCursor(
                    1L,
                    1L,
                    invalidNumber,
                    0,
                    10,
                    "takenCount"
            );

            // when & then
            assertThatThrownBy(cursor::getTakenCount)
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유효하지 않은 숫자 형식입니다. - 보따리 템플릿의 가져간 횟수는 숫자여야 합니다.");
        }
    }
}
