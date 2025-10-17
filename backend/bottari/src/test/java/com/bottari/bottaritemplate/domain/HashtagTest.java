package com.bottari.bottaritemplate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HashtagTest {

    @Nested
    @DisplayName("해시태그 생성")
    class CreateTest {

        @DisplayName("유효한 해시태그를 생성한다.")
        @ParameterizedTest
        @ValueSource(strings = {"여행", "캠핑", "travel", "CAMPING", "여행123", "travel24", "캠핑_준비", "123_test", "한글Eng"})
        void create_Success(final String name) {
            // when
            final Hashtag hashTag = new Hashtag(name);

            // then
            assertThat(hashTag.getName()).isEqualTo(name);
        }

        @DisplayName("해시태그가 null이거나 공백인 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        void create_Exception_Blank(final String name) {
            // when & then
            assertThatThrownBy(() -> new Hashtag(name))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그는 공백일 수 없습니다.");
        }

        @DisplayName("해시태그가 null인 경우, 예외를 던진다.")
        @Test
        void create_Exception_Null() {
            // when & then
            assertThatThrownBy(() -> new Hashtag(null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그는 공백일 수 없습니다.");
        }

        @DisplayName("해시태그에 공백이 포함된 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {"여행 캠핑", "travel test", "캠핑\t준비", "여행\n리스트", " 여행", "캠핑 "})
        void create_Exception_ContainsWhitespace(final String name) {
            // when & then
            assertThatThrownBy(() -> new Hashtag(name))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그에는 공백을 포함할 수 없습니다.");
        }

        @DisplayName("해시태그가 2글자 미만인 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {"a", "1", "가", "_"})
        void create_Exception_TooShort(final String name) {
            // when & then
            assertThatThrownBy(() -> new Hashtag(name))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그 제목이 너무 짧습니다. - 최소 2자 이상 입력 가능합니다.");
        }

        @DisplayName("해시태그가 10글자를 초과하는 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {"12345678901", "가나다라마바사아자차카", "travel_2024_long"})
        void create_Exception_TooLong(final String name) {
            // when & then
            assertThatThrownBy(() -> new Hashtag(name))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그 제목이 너무 깁니다. - 최대 10자까지 입력 가능합니다.");
        }

        @DisplayName("해시태그에 특수문자(언더스코어 제외)가 포함된 경우, 예외를 던진다.")
        @ParameterizedTest
        @ValueSource(strings = {"여행!", "캠핑@준비", "travel#tag", "test$name", "tag%ing", "name^tag",
                "tag&name", "test*tag", "tag()", "tag[]", "tag{}", "tag|name", "tag\\name",
                "tag:name", "tag;name", "tag'name", "tag\"name", "tag<name", "tag>name",
                "tag?name", "tag/name", "tag+name", "tag=name", "tag~name", "tag`name", "tag.name", "tag,name"})
        void create_Exception_InvalidCharacter(final String name) {
            // when & then
            assertThatThrownBy(() -> new Hashtag(name))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해시태그는 한글, 영문, 숫자, 언더스코어(_)만 사용할 수 있습니다.");
        }
    }
}
