package com.bottari.logger

import android.content.Context
import com.bottari.logger.handler.AnalyticsLogHandler
import com.bottari.logger.handler.ConsoleLogHandler
import com.bottari.logger.handler.CrashlyticsLogHandler
import com.bottari.logger.model.LogEventData
import com.bottari.logger.model.LogLevel
import com.bottari.logger.model.UiEventType
import com.bottari.logger.policy.DefaultLogPolicy
import com.bottari.logger.policy.LogPolicy
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * 앱 전역 로깅을 담당하는 객체.
 *
 * Timber를 기반으로 Console, FirebaseAnalytics, FirebaseCrashlytics 핸들러를 활용하여
 * 로그를 분산 전송할 수 있도록 구성된다.
 *
 * Debug/Release 빌드에 따라 로그 수집 여부 및 대상이 달라지며,
 * 로그 레벨에 따라 콘솔, Crashlytics, Analytics로 전송 여부를 제어한다.
 */
object BottariLogger {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics

    /**
     * 로깅 시스템을 초기화한다.
     *
     * - Firebase Analytics / Crashlytics 인스턴스를 설정하고 사용자 ID를 연동한다.
     * - 디버그 여부에 따라 로그 수집 정책을 설정한다.
     * - Timber에 커스텀 Tree([BottariTree])를 심는다.
     *
     * @param context 애플리케이션 컨텍스트
     * @param userId 현재 사용자 ID (Crashlytics/Analytics에 설정됨)
     */
    fun init(
        context: Context,
        userId: String,
    ) {
        analytics = FirebaseAnalytics.getInstance(context)
        crashlytics = FirebaseCrashlytics.getInstance()
        settingFirebase(userId)

        val policy = DefaultLogPolicy(BuildConfig.DEBUG)
        val handlers = createLogHandlers(policy)

        Timber.plant(BottariTree(handlers))
    }

    /**
     * UI 관련 로그를 출력한다.
     *
     * 주로 화면 전환, 사용자 인터랙션 등의 UI 이벤트 로깅에 사용한다.
     * Release 빌드 시 Firebase Analytics에 메시지가 수집된다.
     *
     * @param eventName 이벤트 이름
     * @param params 이벤트에 필요한 추가 정보 (optional)
     */
    fun ui(
        eventName: String,
        params: Map<String, Any>,
    ) = log(LogLevel.UI, LogEventData(eventName, params).serialize())

    fun ui(
        type: UiEventType,
        params: Map<String, Any>,
    ) = log(LogLevel.UI, LogEventData(type.name, params).serialize())

    fun ui(eventData: LogEventData) = log(LogLevel.UI, eventData.serialize())

    /**
     * 데이터 처리 관련 로그를 출력한다.
     *
     * UseCase, Repository 등의 비즈니스 로직 처리 흐름 로깅에 적합하다.
     *
     * @param message 로그 메시지
     */
    fun data(message: String) = log(LogLevel.DATA, message)

    /**
     * 네트워크 통신 관련 로그를 출력한다.
     *
     * Retrofit 등의 네트워크 요청 및 응답 결과를 기록하는 데 사용한다.
     *
     * @param message 로그 메시지
     */
    fun network(message: String) = log(LogLevel.NETWORK, message)

    /**
     * 앱 전역 상태 관련 로그를 출력한다.
     *
     * 예: 앱 진입/종료, 설정 변경 등
     *
     * @param message 로그 메시지
     */
    fun global(message: String) = log(LogLevel.GLOBAL, message)

    /**
     * 디버그용 로그를 출력한다.
     *
     * 개발 중 임시 디버깅 로그를 남길 때 사용하며, 프로덕션에서는 수집되지 않는다.
     *
     * @param message 로그 메시지
     */
    fun debug(message: String) = log(LogLevel.DEBUG, message)

    /**
     * 생명주기 관련 로그를 출력한다.
     *
     * 예: Activity, Fragment의 onCreate/onResume 등의 이벤트 기록
     *
     * @param message 로그 메시지
     */
    fun lifecycle(message: String) = log(LogLevel.LIFECYCLE, message)

    /**
     * 예외 없이 에러 메시지만 출력한다.
     *
     * 간단한 오류나 상태 이상 로그 기록에 사용된다.
     *
     * @param message 에러 메시지
     */
    fun error(message: String) = log(LogLevel.ERROR, message)

    /**
     * 예외와 함께 에러 로그를 출력한다.
     *
     * - 콘솔 출력 외에도 Crashlytics로 전송된다.
     * - tag는 자동으로 "ERROR"로 지정된다.
     *
     * @param message 에러 메시지 (nullable)
     * @param throwable 예외 객체
     */
    fun error(
        message: String?,
        throwable: Throwable,
    ) {
        Timber.tag(LogLevel.ERROR.label).e(throwable, message ?: "")
    }

    /**
     * 일반적인 로그 메시지를 출력한다.
     *
     * INFO 레벨로 로그를 출력하며, tag는 수동으로 입력해야 한다.
     *
     * @param tag 로그 태그
     * @param message 출력할 메시지
     */
    fun log(
        tag: String,
        message: String,
    ) = Timber.tag(tag).i(message)

    /**
     * 로그 핸들러 목록을 생성한다.
     *
     * Console, Crashlytics, Analytics 로그 핸들러를 정책에 따라 구성한다.
     *
     * @param policy 로그 수집 정책 ([LogPolicy])
     * @return 로그 핸들러 리스트
     */
    private fun createLogHandlers(policy: LogPolicy) =
        listOf(
            ConsoleLogHandler(policy),
            CrashlyticsLogHandler(crashlytics, policy),
            AnalyticsLogHandler(analytics, policy),
        )

    /**
     * Firebase 관련 설정을 수행한다.
     *
     * - 사용자 ID 설정
     * - 디버그 여부에 따라 수집 활성화 여부 설정
     *
     * @param userId 현재 사용자 ID
     */
    private fun settingFirebase(userId: String) {
        crashlytics.setUserId(userId)
        analytics.setUserId(userId)
        crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    /**
     * Timber를 통해 지정한 [LogLevel]로 로그를 출력한다.
     *
     * - tag는 level.label을 사용한다.
     * - priority에 따라 로그 레벨을 결정한다.
     *
     * @param level 로그 레벨 정의
     * @param message 로그 메시지
     */
    private fun log(
        level: LogLevel,
        message: String,
    ) {
        Timber.tag(level.label).log(level.priority, message)
    }
}
