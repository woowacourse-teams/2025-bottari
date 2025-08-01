package com.bottari.log;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class HttpLogEntry {

    private final String startTime;
    private final String endTime;
    private final long duration;
    private final String httpMethod;
    private final String requestUri;
    private final String requestHeaders;
    private final String requestBody;
    private final int statusCode;
    private final String responseHeaders;
    private final String responseBody;

    public String toLogString() {
        return String.format("""
                        
                        ==================== HTTP LOG ====================
                        Start Time     : %s
                        End Time       : %s
                        Duration       : %d ms
                        
                        [ Request ]
                          HTTP Method  : %s
                          Request URI  : %s
                          Headers      :
                        %s
                          Body         :
                        %s
                        
                        [ Response ]
                          Status Code  : %d
                          Headers      :
                        %s
                          Body         :
                        %s
                        ==================================================
                        """,
                startTime,
                endTime,
                duration,
                httpMethod,
                requestUri,
                requestHeaders,
                requestBody,
                statusCode,
                responseHeaders,
                responseBody
        );
    }
}
