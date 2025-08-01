package com.bottari.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    BOTTARI_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리를 찾을 수 없습니다."),
    BOTTARI_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리 템플릿을 찾을 수 없습니다."),
    BOTTARI_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "보따리 물품을 찾을 수 없습니다."),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "알람을 찾을 수 없습니다."),

    MEMBER_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 이름입니다."),
    MEMBER_SSAID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 ssaid입니다."),
    BOTTARI_ITEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 보따리 물품입니다."),

    BOTTARI_ITEM_DUPLICATED_IN_REQUEST(HttpStatus.BAD_REQUEST, "요청에 중복된 보따리 물품이 있습니다."),
    BOTTARI_TEMPLATE_ITEM_DUPLICATED_IN_REQUEST(HttpStatus.BAD_REQUEST, "요청에 중복된 보따리 템플릿 물품이 있습니다."),

    BOTTARI_ITEM_MAXIMUM_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "보따리 물품의 최대 개수를 초과했습니다."),
    BOTTARI_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 보따리에 접근할 수 있는 권한이 없습니다."),
    BOTTARI_TEMPLATE_NOT_OWNED(HttpStatus.FORBIDDEN, "해당 보따리 템플릿에 접근할 수 있는 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
