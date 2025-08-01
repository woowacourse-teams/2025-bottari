package com.bottari.log.entry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionLogEntry {

    private String exceptionType;
    private String message;
    private String at;
    private String stackTrace;

    public String toLogString() {
        return String.format("""

                        ==================== EXCEPTION LOG ====================
                        Exception Type : %s
                        Message        : %s
                        At             : %s
                        Stack Trace    : %s
                        =======================================================
                        """,
                exceptionType,
                message,
                at,
                stackTrace
        );
    }
}
