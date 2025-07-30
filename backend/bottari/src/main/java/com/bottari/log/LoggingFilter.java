package com.bottari.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Enumeration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@WebFilter("/*")
public class LoggingFilter extends OncePerRequestFilter {

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
            doLog(startTime, endTime, wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String uri = request.getRequestURI();

        return uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs");
    }

    private void doLog(
            final long startTime,
            final long endTime,
            final ContentCachingRequestWrapper request,
            final ContentCachingResponseWrapper response
    ) {
        final HttpLogEntry logEntry = HttpLogEntry.builder()
                .startTime(toIsoTime(startTime))
                .endTime(toIsoTime(endTime))
                .duration(endTime - startTime)
                .httpMethod(request.getMethod())
                .requestUri(extractUriWithQueryParams(request))
                .requestHeaders(indent(getRequestHeaders(request)))
                .requestBody(new String(request.getContentAsByteArray(), StandardCharsets.UTF_8))
                .statusCode(response.getStatus())
                .responseHeaders(indent(getResponseHeaders(response)))
                .responseBody(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8))
                .build();
        log.info(logEntry.toLogString());
    }

    private String toIsoTime(final Long epochMilli) {
        if (epochMilli == null) {
            return "null";
        }

        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .toString();
    }

    private String extractUriWithQueryParams(final ContentCachingRequestWrapper request) {
        final String queryParams = request.getQueryString();
        if (queryParams != null) {
            return request.getRequestURI() + "?" + queryParams;
        }

        return request.getRequestURI();
    }

    private String getRequestHeaders(final HttpServletRequest request) {
        final StringBuilder headers = new StringBuilder();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headers.append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("\n");
        }

        return headers.toString();
    }

    private String getResponseHeaders(final HttpServletResponse response) {
        final StringBuilder headers = new StringBuilder();
        final Collection<String> headerNames = response.getHeaderNames();
        for (final String headerName : headerNames) {
            headers.append(headerName)
                    .append(": ")
                    .append(response.getHeader(headerName))
                    .append("\n");
        }

        return headers.toString();
    }

    private String indent(final String headers) {
        if (headers == null) {
            return "";
        }

        return headers.replaceAll("(?m)^", "    ");
    }
}
