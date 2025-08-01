package com.bottari.log;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RuntimeExceptionLogEntry {

    private String timestamp;
    private String exceptionType;
    private String message;
    private String at;

    public String toLogString() {
        return String.format("""
                        
                        ================ RUNTIME EXCEPTION LOG ================
                        Timestamp      : %s
                        Exception Type : %s
                        Message        : %s
                        At             : %s
                        =======================================================
                        """,
                timestamp,
                exceptionType,
                message,
                at
        );
    }
}
