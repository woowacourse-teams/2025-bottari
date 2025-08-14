package com.bottari.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== MEMBER 관련 =====
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    MEMBER_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이름입니다."),
    MEMBER_SSAID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 ssaid입니다."),
    MEMBER_NAME_UNCHANGED(HttpStatus.BAD_REQUEST, "기존의 사용자 이름과 동일한 이름으로는 변경할 수 없습니다."),
    MEMBER_NAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "사용자 이름이 너무 짧습니다."),
    MEMBER_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "사용자 이름이 너무 깁니다."),
    MEMBER_NAME_OFFENSIVE(HttpStatus.BAD_REQUEST,"이름에 욕설을 입력할 수 없습니다."),
    MEMBER_NAME_GENERATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "고유한 임시 닉네임을 생성하는 데 실패했습니다. (관리자 문의 필요)"),
    MEMBER_IDENTIFIER_NOT_FOUND_IN_REQUEST(HttpStatus.BAD_REQUEST, "요청에 사용자 식별자가 존재하지 않습니다."),

    // ===== BOTTARI 관련 =====
    BOTTARI_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리를 찾을 수 없습니다."),
    BOTTARI_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 보따리에 접근할 수 있는 권한이 없습니다."),
    BOTTARI_TITLE_UNCHANGED(HttpStatus.BAD_REQUEST, "기존의 보따리 이름과 동일한 이름으로는 변경할 수 없습니다."),
    BOTTARI_TITLE_BLANK(HttpStatus.BAD_REQUEST, "보따리 제목은 공백일 수 없습니다."),
    BOTTARI_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "보따리 제목이 너무 깁니다."),

    // ===== ITEM_NAME 관련 =====
    ITEM_NAME_BLANK(HttpStatus.BAD_REQUEST,"물품명은 공백일 수 없습니다."),
    ITEM_NAME_TOO_LONG(HttpStatus.BAD_REQUEST,"물품명이 너무 깁니다. 최대 20자까지 입력 가능합니다."),
    ITEM_NAME_OFFENSIVE(HttpStatus.BAD_REQUEST,"물품명에 욕설을 입력할 수 없습니다."),

    // ===== BOTTARI_ITEM 관련 =====
    BOTTARI_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리 물품을 찾을 수 없습니다."),
    BOTTARI_ITEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 보따리 물품입니다."),
    BOTTARI_ITEM_DUPLICATE_IN_REQUEST(HttpStatus.BAD_REQUEST, "요청에 중복된 보따리 물품이 있습니다."),
    BOTTARI_ITEM_MAXIMUM_EXCEEDED(HttpStatus.BAD_REQUEST, "보따리 물품의 최대 개수를 초과했습니다."),
    BOTTARI_ITEM_NOT_IN_BOTTARI(HttpStatus.BAD_REQUEST, "보따리 안에 없는 물품입니다."),
    BOTTARI_ITEM_ALREADY_CHECKED(HttpStatus.CONFLICT, "해당 보따리 물품은 이미 체크되어 있습니다."),
    BOTTARI_ITEM_ALREADY_UNCHECKED(HttpStatus.CONFLICT, "해당 보따리 물품은 이미 체크 해제되어 있습니다."),
    BOTTARI_ITEM_CHECK_STATE_INVALID(HttpStatus.BAD_REQUEST, "해당 보따리 물품의 체크 상태가 이미 요청된 상태입니다."),
    BOTTARI_ITEM_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 보따리 물품에 접근할 수 있는 권한이 없습니다."),

    // ===== BOTTARI_TEMPLATE 관련 =====
    BOTTARI_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리 템플릿을 찾을 수 없습니다."),
    BOTTARI_TEMPLATE_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 보따리 템플릿에 접근할 수 있는 권한이 없습니다."),
    BOTTARI_TEMPLATE_TITLE_BLANK(HttpStatus.BAD_REQUEST, "보따리 템플릿 제목은 공백일 수 없습니다."),
    BOTTARI_TEMPLATE_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "보따리 템플릿 제목이 너무 깁니다."),
    BOTTARI_TEMPLATE_INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 보따리 템플릿 정렬 타입입니다."),
    BOTTARI_TEMPLATE_ALREADY_TAKEN_RECENTLY(HttpStatus.CONFLICT, "최근에 해당 보따리 템플릿을 가져온 기록이 있습니다. 잠시 후 다시 시도해주세요."),

    // ===== BOTTARI_TEMPLATE_ITEM 관련 =====
    BOTTARI_TEMPLATE_ITEM_DUPLICATE_IN_REQUEST(HttpStatus.BAD_REQUEST, "요청에 중복된 보따리 템플릿 물품이 있습니다."),

    // ===== TEAM_BOTTARI 관련 =====
    TEAM_BOTTARI_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 보따리를 찾을 수 없습니다."),
    TEAM_BOTTARI_TITLE_BLANK(HttpStatus.BAD_REQUEST, "팀 보따리 제목은 공백일 수 없습니다."),
    TEAM_BOTTARI_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "팀 보따리 제목이 너무 깁니다."),
    TEAM_BOTTARI_INVITE_CODE_GENERATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE,
            "고유한 팀 보따리 초대 코드를 생성하는 데 실패했습니다. (관리자 문의 필요)"),

    // ===== TEAM_BOTTARI_ITEM 관련 =====
    TEAM_BOTTARI_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "팀 보따리 물품을 찾을 수 없습니다."),
    TEAM_BOTTARI_ITEM_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다."),
    TEAM_BOTTARI_ITEM_ALREADY_CHECKED(HttpStatus.CONFLICT, "해당 팀 보따리 물품은 이미 체크되어 있습니다."),
    TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED(HttpStatus.CONFLICT, "해당 팀 보따리 물품은 이미 체크 해제되어 있습니다."),

    // ===== TEAM_MEMBER 관련 =====
    MEMBER_NOT_IN_TEAM_BOTTARI(HttpStatus.FORBIDDEN, "해당 팀 보따리의 팀 멤버가 아닙니다."),

    // ===== ALARM 관련 =====
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "알람을 찾을 수 없습니다."),
    ALARM_ALREADY_ACTIVE(HttpStatus.CONFLICT, "알람이 이미 활성화되어 있습니다."),
    ALARM_ALREADY_INACTIVE(HttpStatus.CONFLICT, "알람이 이미 비활성화되어 있습니다."),
    ALARM_LOCATION_REQUIRES_ROUTINE(HttpStatus.BAD_REQUEST, "루틴 알람이 설정되지 않으면 위치 알람을 설정할 수 없습니다."),

    // ===== REPORT 관련 =====
    REPORT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 템플릿에 대한 신고 기록이 있습니다."),

    // ===== FCM 관련 =====
    FCM_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "FCM 토큰 정보가 존재하지 않습니다."),
    FCM_INITIALIZED_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Firebase 초기화를 실패하였습니다."),
    FCM_MESSAGE_SEND_FAIL(HttpStatus.BAD_GATEWAY, "FCM 서버 문제로 FCM 메시지 전송을 실패하였습니다."),
    FCM_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰으로 인해 FCM 메시지 전송을 실패하였습니다."),

    // ===== 기타 =====
    DATE_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),
    NUMBER_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 숫자 형식입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
