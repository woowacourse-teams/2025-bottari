package com.bottari.log.entry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessExceptionLogEntry {

    private String exceptionType;
    private String message;
    private String at;

    public String toLogString() {
        return String.format("""
                        
                        ================ BUSINESS EXCEPTION LOG ===============
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
