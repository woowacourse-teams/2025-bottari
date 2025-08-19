package com.bottari.log.entry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessExceptionLogEntry {

    private String exceptionType;
    private String message;
    private String httpMethod;
    private String requestUri;
    private String ssaid;

    public String toLogString() {
        return String.format("""
                        
                        ================ BUSINESS EXCEPTION LOG ===============
                        Exception Type : %s
                        Message        : %s
                        HTTP Method    : %s
                        Request URI    : %s
                        Ssaid          : %s
                        =======================================================
                        """,
                exceptionType,
                message,
                httpMethod,
                requestUri,
                ssaid
        );
    }
}
