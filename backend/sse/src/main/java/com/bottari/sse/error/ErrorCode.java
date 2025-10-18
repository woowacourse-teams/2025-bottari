package com.bottari.sse.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== PUSH MESSAGE 관련 =====
    INVALID_MESSAGE_FORMAT(HttpStatus.BAD_REQUEST, "메시지 포맷이 올바르지 않습니다."),

    // ===== SSE CONNECTION 관련 =====
    SSE_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 연결에 실패했습니다."),

    // ===== 기타 =====
    INVALID_TOPIC_NAME(HttpStatus.INTERNAL_SERVER_ERROR, "올바르지 않은 토픽 이름입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
