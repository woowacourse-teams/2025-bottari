package com.bottari.domain.model

import com.bottari.domain.model.member.Nickname
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class NicknameTest {
    @DisplayName("닉네임이 생성된다")
    @Test
    fun createNicknameTest() {
        // given
        val test = "test"

        // when
        val nickname = Nickname(test)

        // then
        assertSoftly(nickname) {
            shouldBeInstanceOf<Nickname>()
            value shouldBe test
        }
    }

    @DisplayName("닉네임은 2글자 미만일 수 없다")
    @Test
    fun nicknameLessThanTwoTest() {
        // given
        val nickname = "t"

        // when
        // then
        nickname.length shouldBeLessThan 2
        shouldThrow<IllegalArgumentException> { Nickname(nickname) }
    }

    @DisplayName("닉네임은 10글자를 초과할 수 없다")
    @Test
    fun nicknameMoreThanTenTest() {
        // given
        val nickname = "TestTestTes"

        // when
        // then
        nickname.length shouldBeGreaterThan 10
        shouldThrow<IllegalArgumentException> { Nickname(nickname) }
    }
}
