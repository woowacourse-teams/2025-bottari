package com.bottari.domain.model

import com.bottari.domain.model.member.Member
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MemberTest {
    @DisplayName("멤버를 생성한다")
    @Test
    fun createMemberTest() {
        // given
        val ssaid = "ssaid"
        val nickname = "test"

        // when
        val member = Member(ssaid, nickname)

        // then
        assertSoftly(member) {
            ssaid shouldBe "ssaid"
            nickname shouldBe "test"
        }
    }

    @DisplayName("SSAID가 공백일 경우 멤버를 생성할 수 없다")
    @Test
    fun ssaidEmptyTest() {
        // given
        val ssaid = ""
        val nickname = "test"

        // when
        // then
        shouldThrow<IllegalArgumentException> { Member(ssaid, nickname) }
    }

    @DisplayName("닉네임이 3글자 미만인 경우 멤버를 생성할 수 없다")
    @Test
    fun nicknameLessThanThreeTest() {
        // given
        val ssaid = "ssaid"
        val nickname = "te"

        // when
        // then
        nickname.length shouldBeLessThan 3
        shouldThrow<IllegalArgumentException> { Member(ssaid, nickname) }
    }

    @DisplayName("닉네임이 10글자를 초과한 경우 멤버를 생성할 수 없다")
    @Test
    fun nicknameMoreThanTenTest() {
        // given
        val ssaid = "ssaid"
        val nickname = "TestTestTes"

        // when
        // then
        nickname.length shouldBeGreaterThan 10
        shouldThrow<IllegalArgumentException> { Member(ssaid, nickname) }
    }
}
