package com.bottari.logger

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * 앱 전역에서 사용할 로깅 유틸리티 객체.
 * Timber + FirebaseAnalytics + Crashlytics를 활용한 통합 로깅.
 */
object BottariLogger {
    /**
     * 로깅 시스템 초기화
     * - FirebaseAnalytics와 FirebaseCrashlytics 인스턴스를 설정하고 BottariTree를 Timber에 심는다.
     *
     * @param context 앱 Context
     * @param userId 현재 사용자 ID (Crashlytics user 식별용)
     */
    fun init(
        context: Context,
        userId: String,
    ) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val crashlytics = FirebaseCrashlytics.getInstance()
        val bottariTree = BottariTree(userId, analytics, crashlytics)
        Timber.plant(bottariTree)
    }

    /**
     * 공통 로그 함수. 커스텀 태그를 지정하여 정보 로그를 남길 때 사용.
     *
     * @param tag 로그 태그 (직접 지정)
     * @param message 로그 메시지
     */
    fun log(
        tag: String,
        message: String,
    ) = Timber.tag(tag).i(message)

    /**
     * UI 관련 로그
     * - 화면 전환, View 상태 변경 등 UI 요소 추적 시 사용
     */
    fun ui(message: String) = log(LogLevel.UI, message)

    /**
     * 데이터 처리 관련 로그
     * - 데이터 로드, 저장, 처리 등 데이터 관련 이벤트 추적 시 사용
     */
    fun data(message: String) = log(LogLevel.DATA, message)

    /**
     * 네트워크 요청/응답 관련 로그
     * - Retrofit 요청, 응답, 에러 등 추적 시 사용
     */
    fun network(message: String) = log(LogLevel.NETWORK, message)

    /**
     * 앱 전역 설정 또는 공통 로직 관련 로그
     * - Application, DI, 공통 유틸 등
     */
    fun global(message: String) = log(LogLevel.GLOBAL, message)

    /**
     * 디버깅용 로그
     * - 로컬 테스트나 디버깅 시 자유롭게 사용 가능 (사용 후 제거)
     */
    fun debug(message: String) = log(LogLevel.DEBUG, message)

    /**
     * 생명주기 관련 로그
     * - Activity/Fragment의 onCreate, onResume 등 Lifecycle 추적 시
     */
    fun lifecycle(message: String) = log(LogLevel.LIFECYCLE, message)

    /**
     * 예외 상황이나 에러 처리 관련 로그
     * - 사용자에게 노출되지 않는 오류, 예외 발생 추적 시
     */
    fun error(message: String) = log(LogLevel.ERROR, message)

    /**
     * Throwable과 함께 에러 로그를 남기는 경우
     * - Firebase Crashlytics와 함께 예외를 기록할 때
     *
     * @param message 에러 메시지
     * @param throwable 예외 객체
     */
    fun error(
        message: String?,
        throwable: Throwable,
    ) {
        message.orEmpty().ifEmpty { Timber.tag(LogLevel.ERROR.label).e(throwable) }
        Timber.tag(LogLevel.ERROR.label).e(throwable, message)
    }

    /**
     * 공통 로깅 내부 구현
     * Timber의 log 메서드를 이용해 로그 우선순위(priority)를 지정함
     */
    private fun log(
        level: LogLevel,
        message: String,
    ) {
        Timber.tag(level.label).log(level.priority, message)
    }
}
