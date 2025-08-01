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
    ;

    private final HttpStatus status;
    private final String message;
}
