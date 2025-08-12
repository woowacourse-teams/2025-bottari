package com.bottari.domain.model

import com.bottari.domain.model.team.HeadCount
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class HeadCountTest {
    @DisplayName("인원은 1명 미만일 수 없다")
    @Test
    fun headCountLessThanOneTest() {
        // given
        val value = 0

        // when
        // then
        value.shouldBeLessThan(1)
        shouldThrow<IllegalArgumentException> { HeadCount(value) }
    }

    @DisplayName("인원은 10명을 초과할 수 없다")
    @Test
    fun headCountMoreThanTenTest() {
        // given
        val value = 11

        // when
        // then
        value.shouldBeGreaterThan(10)
        shouldThrow<IllegalArgumentException> { HeadCount(value) }
    }
}
