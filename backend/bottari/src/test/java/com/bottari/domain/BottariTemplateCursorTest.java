package com.bottari.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.dto.ReadNextBottariTemplateRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Pageable;

@DisplayName("BottariTemplateCursor 테스트")
class BottariTemplateCursorTest {

    @Nested
    class FromTest {

        @DisplayName("커서를 생성한다.")
        @ParameterizedTest
        @CsvSource({
                "createdAt, 2024-01-01T00:00:00Z",
                "takenCount, 1"
        })
        void from(
                final String property,
                final String lastInfo
        ) {
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "검색어",
                    1L,
                    lastInfo,
                    0,
                    10,
                    property
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertAll(
                    () -> assertThat(actual.query()).isEqualTo("검색어"),
                    () -> assertThat(actual.lastId()).isEqualTo(1L),
                    () -> assertThat(actual.lastInfo()).isEqualTo(lastInfo),
                    () -> assertThat(actual.page()).isEqualTo(0),
                    () -> assertThat(actual.size()).isEqualTo(10),
                    () -> assertThat(actual.property()).isEqualTo(property)
            );
        }

        @DisplayName("null 및 빈 값들이 기본값으로 할당된다.")
        @Test
        void from_Normalize() {
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    null,          // query
                    null,          // lastId
                    null,          // lastInfo
                    -1,            // page
                    0,             // size
                    null           // property
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertAll(
                    () -> assertThat(actual.query()).isEqualTo(""),
                    () -> assertThat(actual.lastId()).isEqualTo(Long.MAX_VALUE),
                    () -> assertThat(actual.page()).isEqualTo(0),
                    () -> assertThat(actual.size()).isEqualTo(10),
                    () -> assertThat(actual.property()).isEqualTo("createdAt"),
                    () -> assertThat(actual.lastInfo()).isNotNull()
            );
        }

        @DisplayName("createdAt property일 때 기본 lastInfo가 미래 날짜로 설정된다.")
        @Test
        void from_SetDefaultCreatedAt() {
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    null,
                    null,
                    0,
                    10,
                    "createdAt"
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.getCreatedAt()).isAfter(LocalDateTime.now());
        }

        @DisplayName("takenCount property일 때 기본 lastInfo가 Long.MAX_VALUE로 설정된다.")
        @Test
        void from_SetDefaultTakenCount() {
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    null,
                    null,
                    0,
                    10,
                    "takenCount"
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.getTakenCount()).isEqualTo(Long.MAX_VALUE);
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
            final BottariTemplateCursor cursor = new BottariTemplateCursor(
                    "",
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
            final BottariTemplateCursor cursor = new BottariTemplateCursor(
                    "",
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
            final BottariTemplateCursor cursor = new BottariTemplateCursor(
                    "",
                    1L,
                    invalidDate,
                    0,
                    10,
                    "createdAt"
            );

            // when & then
            assertThatThrownBy(cursor::getCreatedAt)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("날짜 형태가 올바르지 않습니다.");
        }
    }

    @Nested
    class GetTakenCountTest {

        @DisplayName("유효한 숫자 문자열을 Long으로 파싱한다.")
        @ParameterizedTest
        @ValueSource(strings = {"0", "1", "100", "9223372036854775807"})
        void getTakenCount(final String numberString) {
            // given
            final BottariTemplateCursor cursor = new BottariTemplateCursor(
                    "",
                    1L,
                    numberString,
                    0,
                    10,
                    "takenCount"
            );

            // when
            final Long actual = cursor.getTakenCount();

            // then
            assertThat(actual).isEqualTo(Long.parseLong(numberString));
        }

        @DisplayName("잘못된 숫자 형식으로 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "invalid-number",
                "12.34",
                "1,000",
                "9223372036854775808" // Long.MAX_VALUE + 1
        })
        void getTakenCount_Exception_InvalidNumberFormat(final String invalidNumber) {
            // given
            final BottariTemplateCursor cursor = new BottariTemplateCursor(
                    "",
                    1L,
                    invalidNumber,
                    0,
                    10,
                    "takenCount"
            );

            // when & then
            assertThatThrownBy(cursor::getTakenCount)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("숫자 형태가 올바르지 않습니다.");
        }
    }

    @Nested
    class NormalizationTest {

        @DisplayName("query 정규화 테스트")
        @Test
        void normalizeQuery() {
            // given
            final String blankQuery = " ";
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    blankQuery,
                    1L,
                    "info",
                    0,
                    10,
                    "createdAt"
            );

            final String expected = "";

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.query()).isEqualTo(expected);
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
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    1L,
                    "info",
                    page,
                    10,
                    "createdAt"
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.page()).isEqualTo(expected);
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
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    1L,
                    null,
                    0,
                    size,
                    "createdAt"
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.size()).isEqualTo(expected);
        }

        @DisplayName("lastId 정규화 테스트")
        @Test
        void normalizeLastId() {
            // given
            final Long nullLastId = null;
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    nullLastId,
                    null,
                    0,
                    10,
                    "createdAt"
            );

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.lastId()).isEqualTo(Long.MAX_VALUE);
        }

        @DisplayName("property 정규화 테스트")
        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n"})
        void normalizeProperty(final String input) {
            // given
            final ReadNextBottariTemplateRequest request = new ReadNextBottariTemplateRequest(
                    "",
                    1L,
                    "info",
                    0,
                    10,
                    input
            );

            final String expected = "createdAt";

            // when
            final BottariTemplateCursor actual = BottariTemplateCursor.from(request);

            // then
            assertThat(actual.property()).isEqualTo(expected);
        }
    }
}
