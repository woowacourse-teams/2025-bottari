package com.bottari;

import com.bottari.log.ExceptionLogEntry;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ProblemDetail handleIllegalException(
            final RuntimeException exception,
            final HttpServletRequest request
    ) {
        doLog(exception, request);

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(
            final Exception exception,
            final HttpServletRequest request
    ) {
        doLog(exception, request);

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "예상치 못한 서버 에러입니다. 관리자(코기)를 찾아가세요."
        );
    }

    private void doLog(
            final RuntimeException exception,
            final HttpServletRequest request
    ) {
        final ExceptionLogEntry logEntry = ExceptionLogEntry.builder()
                .timestamp(toIsoTime(System.currentTimeMillis()))
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .at(request.getRequestURI())
                .build();
        log.info(logEntry.toLogString());
    }

    private void doLog(
            final Exception exception,
            final HttpServletRequest request
    ) {
        final ExceptionLogEntry logEntry = ExceptionLogEntry.builder()
                .timestamp(toIsoTime(System.currentTimeMillis()))
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .at(request.getRequestURI())
                .stackTrace(getStackTrace(exception))
                .build();
        log.info(logEntry.toLogString());
    }

    private String getStackTrace(final Exception exception) {
        return Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
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
}
