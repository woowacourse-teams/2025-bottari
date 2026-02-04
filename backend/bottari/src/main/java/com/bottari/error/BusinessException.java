package com.bottari.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailCause;

    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailCause = "";
    }

    public BusinessException(
            final ErrorCode errorCode,
            final String detailCause
    ) {
        super("%s - %s".formatted(errorCode.getMessage(), detailCause));
        this.errorCode = errorCode;
        this.detailCause = detailCause;
    }
}
