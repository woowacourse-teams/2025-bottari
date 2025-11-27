package com.bottari.core.domain.model

import com.bottari.core.domain.extension.toTimeMillis
import com.bottari.core.domain.model.alarm.AlarmTriggerTimeCalculator
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AlarmTriggerTimeCalculatorTest {
    private val calculator: AlarmTriggerTimeCalculator = AlarmTriggerTimeCalculator()

    @DisplayName("반복 알람 설정 시 다음 알람이 울릴 날짜 및 시간을 반환한다")
    @Test
    fun repeatAlarmTest() {
        // given
        val alarm = ACTIVE_REPEAT_ALARM_FIXTURE
        val baseDateTime = LocalDateTime.of(2025, 11, 27, 18, 0)
        val expectedTime = LocalDateTime.of(2025, 11, 28, 18, 0).toTimeMillis()

        // when
        val triggerTime =
            calculator.calculate(
                alarm = alarm,
                baseDateTime = baseDateTime,
            )

        // then
        assertSoftly(triggerTime) {
            shouldNotBeNull()
            shouldBe(expectedTime)
        }
    }

    @DisplayName("미반복 알람 설정 시 알람이 울릴 날짜 및 시간을 반환한다")
    @Test
    fun nonRepeatAlarmTest() {
        // given
        val alarm = ACTIVE_NON_REPEAT_ALARM_FIXTURE
        val baseDateTime = LocalDateTime.of(2025, 11, 27, 18, 0)
        val expectedTime = LocalDateTime.of(2025, 11, 28, 18, 0).toTimeMillis()

        // when
        val triggerTime =
            calculator.calculate(
                alarm = alarm,
                baseDateTime = baseDateTime,
            )

        // then
        assertSoftly(triggerTime) {
            shouldNotBeNull()
            shouldBe(expectedTime)
        }
    }

    @DisplayName("이전 시간의 미반복 알람을 설정하면 null을 반환한다")
    @Test
    fun unavailableAlarmTest() {
        // given
        val alarm = ACTIVE_NON_REPEAT_ALARM_FIXTURE
        val baseDateTime = LocalDateTime.of(2025, 11, 28, 18, 1)

        // when
        val triggerTime =
            calculator.calculate(
                alarm = alarm,
                baseDateTime = baseDateTime,
            )

        // then
        triggerTime.shouldBeNull()
    }

    @DisplayName("비활성화된 알람을 전달하면 null을 반환한다")
    @Test
    fun nonActiveAlarmTest() {
        // given
        val alarm = NON_ACTIVE_ALARM_FIXTURE

        // when
        val triggerTime = calculator.calculate(alarm)

        // then
        triggerTime.shouldBeNull()
    }
}
