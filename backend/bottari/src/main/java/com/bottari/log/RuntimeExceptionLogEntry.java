package com.bottari.log;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RuntimeExceptionLogEntry {

    private String exceptionType;
    private String message;
    private String at;

    public String toLogString() {
        return String.format("""
                        
                        ================ RUNTIME EXCEPTION LOG ================
                        Exception Type : %s
                        Message        : %s
                        At             : %s
                        =======================================================
                        """,
                exceptionType,
                message,
                at
        );
    }
}
