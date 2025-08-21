package com.bottari.logger.model

/**
 * UI 이벤트 타입을 나타내는 enum 클래스.
 *
 * 각 이벤트 타입별로 수집해야 하는 파라미터가 다릅니다.
 * 아래 각 상수 위 주석을 참고해 필요한 파라미터를 포함해주세요.
 */
enum class UiEventType {
    /**
     * 화면 진입 이벤트
     *
     * 파라미터:
     * - screen: 진입한 화면명
     */
    SCREEN_ENTER,

    /**
     * 화면 퇴장 이벤트
     *
     * 파라미터:
     * - screen: 퇴장한 화면명
     * - stay_duration: 머문 시간
     */
    SCREEN_EXIT,

    /**
     * 클릭 이벤트
     *
     * 파라미터:
     * - target: 클릭한 대상명
     * - screen: 클릭이 발생한 화면명
     */
    CLICK,

    /**
     * 개인 보따리 생성 이벤트
     *
     * 파라미터:
     * - bottari_id: 생성된 보따리 고유 ID
     * - bottari_title: 보따리 제목
     */
    PERSONAL_BOTTARI_CREATE,

    /**
     * 개인 보따리 삭제 이벤트
     *
     * 파라미터:
     * - bottari_id: 삭제된 보따리 고유 ID
     * - bottari_title: 삭제된 보따리 제목
     */
    PERSONAL_BOTTARI_DELETE,

    /**
     * 개인 보따리 생성 이벤트
     *
     * 파라미터:
     * - bottari_id: 생성된 보따리 고유 ID
     * - old_title: 이전 보따리 제목
     * - new_title: 변경 후 보따리 제목
     */
    PERSONAL_BOTTARI_TITLE_EDIT,

    /**
     * 개인 보따리 아이템 편집 이벤트
     *
     * 파라미터:
     * - bottari_id: 편집된 보따리 고유 ID
     * - old_items: 이전 아이템 리스트
     * - new_items: 변경 후 아이템 리스트
     */
    PERSONAL_BOTTARI_ITEM_EDIT,

    /**
     * 팀 보따리 생성 이벤트
     *
     * 파라미터:
     * - bottari_id: 생성된 보따리 고유 ID
     * - bottari_title: 보따리 제목
     */
    TEAM_BOTTARI_CREATE,

    /**
     * 팀 보따리 입장 이벤트
     *
     * 파라미터:
     * - bottari_id: 생성된 보따리 고유 ID
     */
    TEAM_BOTTARI_JOIN,

    /**
     * 팀 보따리 나가기 이벤트
     *
     * 파라미터
     * - bottari_id: 삭제된 보따리 고유 ID
     * - bottari_title: 삭제된 보따리 제목
     */
    TEAM_BOTTARI_EXIT,

    /**
     * 체크리스트 완료 이벤트
     *
     * 파라미터:
     * - bottari_id: 보따리 고유 ID
     * - completed_item_count: 완료된 항목 수
     */
    CHECKLIST_COMPLETE,

    /**
     * 팀원 조회 이벤트
     *
     * 파라미터:
     * - invite_code: 초대 코드
     * - member_head_count: 인원 수
     * - host_name: 방장 닉네임
     * - members: 팀원 명단
     */
    TEAM_BOTTARI_MEMBERS_FETCH,

    /**
     * 모두의 보따리 템플릿 검색 이벤트
     *
     * 파라미터:
     * - query: 검색어
     * - result_count: 검색 결과 개수
     */
    TEMPLATE_SEARCH,

    /**
     * 모두의 보따리 템플릿 스크롤 이벤트
     *
     * 파라미터:
     * - page: 스크롤된 페이지 번호
     */
    TEMPLATE_SCROLL,

    /**
     * 모두의 보따리 템플릿 선택 이벤트
     *
     * 파라미터:
     * - template_id: 템플릿 고유 ID
     * - template_title: 템플릿 제목
     * - template_items: 템플릿에 포함된 아이템 리스트
     */
    TEMPLATE_TAKE,

    /**
     * 모두의 보따리 템플릿 업로드 이벤트
     *
     * 파라미터:
     * - template_id: 템플릿 고유 ID
     * - template_title: 템플릿 제목
     * - template_items: 템플릿에 포함된 아이템 리스트
     */
    TEMPLATE_UPLOAD,

    /**
     * 모두의 보따리 템플릿 삭제 이벤트
     *
     * 파라미터:
     * - template_id: 템플릿 고유 ID
     * - template_title: 템플릿 제목
     * - template_items: 템플릿에 포함된 아이템 리스트
     */
    TEMPLATE_DELETE,

    /**
     * 모두의 보따리 템플릿 신고 이벤트
     *
     * 파라미터:
     * - template_id: 템플릿 고유 ID
     * - report_reason: 신고 사유
     */
    TEMPLATE_REPORT,

    /**
     * 프로필 닉네임 편집 이벤트
     *
     * 파라미터:
     * - old_nickname: 이전 닉네임
     * - new_nickname: 변경된 닉네임
     */
    NICKNAME_EDIT,

    /**
     * 알람 생성 이벤트
     *
     * 파라미터:
     * - alarm_id: 알람 고유 ID
     * - alarm_info: 알람 상세 정보
     */
    ALARM_CREATE,

    /**
     * 알람 편집 이벤트
     *
     * 파라미터:
     * - alarm_id: 알람 고유 ID
     * - old_alarm_info: 편집 전 알람 정보
     * - new_alarm_info: 편집 후 알람 정보
     */
    ALARM_EDIT,

    /**
     * 알람 활성화 이벤트
     *
     * 파라미터:
     * - alarm_id: 알람 고유 ID
     */
    ALARM_ACTIVE,

    /**
     * 알람 비활성화 이벤트
     *
     * 파라미터:
     * - alarm_id: 알람 고유 ID
     */
    ALARM_INACTIVE,

    /**
     * 푸시 알림 생성 이벤트
     *
     * 파라미터:
     * - notification_id: 알림 고유 ID
     * - time: 알림 생성 시간
     */
    NOTIFICATION_CREATE,

    /**
     * 푸시 알림 클릭 이벤트
     *
     * 파라미터:
     * - notification_id: 알림 고유 ID
     * - time: 알림 클릭 시간
     */
    NOTIFICATION_CLICK,
}
