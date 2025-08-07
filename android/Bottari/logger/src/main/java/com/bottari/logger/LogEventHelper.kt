package com.bottari.logger

import com.bottari.logger.model.UiEventType

/**
 * UI 이벤트 로깅 헬퍼
 *
 * - screen 이름을 공통 파라미터로 자동 주입
 * - 추가 파라미터는 Map으로 쉽게 전달 가능
 * - Firebase Analytics 이벤트 호출 래핑
 */
object LogEventHelper {
    /**
     * 공통으로 들어갈 파라미터 키
     */
    private const val PARAM_SCREEN = "screen"
    private const val PARAM_TARGET = "target"

    /**
     * 이벤트 로깅
     *
     * @param eventType UiEventType enum
     * @param screen 현재 화면 이름 (ex: "MarketMain")
     * @param additionalParams 추가 파라미터 (선택)
     */
    fun logEvent(
        eventType: UiEventType,
        screen: String,
        additionalParams: Map<String, Any> = emptyMap(),
    ) {
        val params =
            mutableMapOf<String, Any>(
                PARAM_SCREEN to screen,
            )
        params.putAll(additionalParams)
        BottariLogger.ui(eventType.name, params)
    }

    /**
     * 클릭 이벤트 편의 함수
     *
     * @param screen 현재 화면 이름
     * @param target 클릭 대상 이름 (ex: "save_button")
     */
    fun logClick(
        screen: String,
        target: String,
    ) {
        logEvent(
            eventType = UiEventType.CLICK,
            screen = screen,
            additionalParams = mapOf(PARAM_TARGET to target),
        )
    }

    /**
     * 화면 진입 이벤트 편의 함수
     *
     * @param screen 현재 화면 이름
     */
    fun logScreenEnter(screen: String) {
        logEvent(
            eventType = UiEventType.SCREEN_ENTER,
            screen = screen,
        )
    }
}
