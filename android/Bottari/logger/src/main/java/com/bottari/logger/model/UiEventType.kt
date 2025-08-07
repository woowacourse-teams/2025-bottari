package com.bottari.logger.model

enum class UiEventType(
    private val eventName: String,
) {
    // 공통
    SCREEN_ENTER("screen_enter"), // param: screen
    CLICK("click"), // param: target, screen

    // Bottari 관련
    PERSONAL_BOTTARI_CREATE("personal_bottari_create"), // param: bottari_id, bottari_title
    PERSONAL_BOTTARI_DELETE("personal_bottari_delete"), // param: bottari_id
    PERSONAL_BOTTARI_ITEM_EDIT("personal_bottari_item_edit"), // param: bottari_id, old_items, new_items

    // 체크리스트 관련
    CHECKLIST_COMPLETE("checklist_complete"), // param: checklist_id, completed_item_count

    // 모두의 보따리 관련
    TEMPLATE_SEARCH("template_search"), // param: query, result_count
    TEMPLATE_SCROLL("template_scroll"), // param: page
    TEMPLATE_TAKE("template_take"), // param: template_id, template_title, template_items
    TEMPLATE_UPLOAD("template_upload"), // param: template_id, template_title, template_items
    TEMPLATE_REPORT("template_report"), // param: template_id, report_reason

    // 프로필 관련
    NICKNAME_EDIT("nickname_edit"), // param: old_nickname, new_nickname

    // 알람 관련
    ALARM_CREATE("alarm_create"), // param: alarm_id, alarm_info
    ALARM_EDIT("alarm_edit"), // param: alarm_id, old_alarm_info, new_alarm_info
    ALARM_ACTIVE("alarm_active"), // param: alarm_id
    ALARM_INACTIVE("alarm_inactive"), // param: alarm_id

    // 푸시 알림 관련
    NOTIFICATION_CREATE("notification_create"), // param: notification_id, time
    NOTIFICATION_CLICK("notification_click"), // param: notification_id, time
    ;

    override fun toString(): String = eventName
}
