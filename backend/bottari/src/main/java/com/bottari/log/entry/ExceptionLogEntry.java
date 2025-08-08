package com.bottari.log.entry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionLogEntry {

    private String exceptionType;
    private String message;
    private String stackTrace;
    private String httpMethod;
    private String requestUri;
    private String ssaid;

    public String toLogString() {
        return String.format("""

                        ==================== EXCEPTION LOG ====================
                        Exception Type : %s
                        Message        : %s
                        Stack Trace    : %s
                        HTTP Method    : %s
                        Request URI    : %s
                        Ssaid          : %s
                        =======================================================
                        """,
                exceptionType,
                message,
                stackTrace,
                httpMethod,
                requestUri,
                ssaid
        );
    }
}
