package com.bottari.log;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionLogEntry {

    private String timestamp;
    private String exceptionType;
    private String message;
    private String at;
    private String stackTrace;

    public String toLogString() {
        return String.format("""
                        
                        ==================== EXCEPTION LOG ====================
                        Timestamp      : %s
                        Exception Type : %s
                        Message        : %s
                        At             : %s
                        Stack Trace    : %s
                        =======================================================
                        """,
                timestamp,
                exceptionType, message,
                at, stackTrace
        );
    }
}
