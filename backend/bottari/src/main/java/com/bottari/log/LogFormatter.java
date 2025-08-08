package com.bottari.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LogFormatter {

    public String toStackTraceLog(final Exception exception) {
        return Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    public String toUriWithQueryParamsLog(final HttpServletRequest request) {
        final String queryParams = request.getQueryString();
        if (queryParams != null) {
            return request.getRequestURI() + "?" + queryParams;
        }

        return request.getRequestURI();
    }

    public String toRequestHeadersLog(final HttpServletRequest request) {
        final StringBuilder headers = new StringBuilder();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headers.append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("\n");
        }

        return indent(headers.toString());
    }

    public String toRequestBodyLog(final ContentCachingRequestWrapper request) {
        final byte[] contentAsByteArray = request.getContentAsByteArray();

        return new String(contentAsByteArray, StandardCharsets.UTF_8);
    }

    public String toSsaidLog(final HttpServletRequest request) {
        final String ssaid = request.getHeader("ssaid");
        if (ssaid == null || ssaid.isEmpty()) {
            return "GUEST";
        }
        return ssaid;
    }

    public String toResponseHeadersLog(final HttpServletResponse response) {
        final StringBuilder headers = new StringBuilder();
        final Collection<String> headerNames = response.getHeaderNames();
        for (final String headerName : headerNames) {
            headers.append(headerName)
                    .append(": ")
                    .append(response.getHeader(headerName))
                    .append("\n");
        }

        return indent(headers.toString());
    }

    public String toResponseBodyLog(final ContentCachingResponseWrapper request) {
        final byte[] contentAsByteArray = request.getContentAsByteArray();

        return new String(contentAsByteArray, StandardCharsets.UTF_8);
    }

    private String indent(final String log) {
        if (log == null) {
            return "";
        }

        return log.replaceAll("(?m)^", "    ");
    }
}
