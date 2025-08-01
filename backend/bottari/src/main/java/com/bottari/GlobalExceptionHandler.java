package com.bottari;

import com.bottari.log.ExceptionLogEntry;
import com.bottari.log.LogFormatter;
import com.bottari.log.RuntimeExceptionLogEntry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogFormatter formatter;

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
        final RuntimeExceptionLogEntry logEntry = RuntimeExceptionLogEntry.builder()
                .timestamp(formatter.toIsoTimeLog(System.currentTimeMillis()))
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .at(request.getRequestURI())
                .build();
        log.warn(logEntry.toLogString());
    }

    private void doLog(
            final Exception exception,
            final HttpServletRequest request
    ) {
        final ExceptionLogEntry logEntry = ExceptionLogEntry.builder()
                .timestamp(formatter.toIsoTimeLog(System.currentTimeMillis()))
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .at(request.getRequestURI())
                .stackTrace(formatter.toStackTraceLog(exception))
                .build();
        log.error(logEntry.toLogString());
    }
}
