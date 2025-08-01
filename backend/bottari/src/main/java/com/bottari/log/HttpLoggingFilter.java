package com.bottari.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@WebFilter("/*")
@RequiredArgsConstructor
public class HttpLoggingFilter extends OncePerRequestFilter {

    private final LogFormatter formatter;

    @Override
    public void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            final FilterChain chain
    ) throws ServletException, IOException {
        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        final long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            final long endTime = System.currentTimeMillis();
            doLog(endTime - startTime, wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String uri = request.getRequestURI();

        return uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs");
    }

    private void doLog(
            final long duration,
            final ContentCachingRequestWrapper request,
            final ContentCachingResponseWrapper response
    ) {
        final HttpLogEntry logEntry = HttpLogEntry.builder()
                .duration(duration)
                .httpMethod(request.getMethod())
                .requestUri(formatter.toUriWithQueryParamsLog(request))
                .requestHeaders(formatter.toRequestHeadersLog(request))
                .requestBody(formatter.toRequestBodyLog(request))
                .statusCode(response.getStatus())
                .responseHeaders(formatter.toResponseHeadersLog(response))
                .responseBody(formatter.toResponseBodyLog(response))
                .build();
        log.info(logEntry.toLogString());
    }
}
