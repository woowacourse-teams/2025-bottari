package com.bottari.error;

import com.bottari.log.entry.ExceptionLogEntry;
import com.bottari.log.LogFormatter;
import com.bottari.log.entry.BusinessExceptionLogEntry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogFormatter formatter;

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(
            final BusinessException exception,
            final HttpServletRequest request
    ) {
        doLog(exception, request);
        final ErrorCode errorCode = exception.getErrorCode();
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getStatus(),
                exception.getMessage()
        );
        problemDetail.setTitle(errorCode.name());

        return problemDetail;
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
        final BusinessExceptionLogEntry logEntry = BusinessExceptionLogEntry.builder()
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .httpMethod(request.getMethod())
                .requestUri(formatter.toUriWithQueryParamsLog(request))
                .ssaid(formatter.toSsaidLog(request))
                .build();
        final Marker businessExceptionLogMarker = MarkerFactory.getMarker("BUSINESS-EXCEPTION-LOG");
        log.warn(businessExceptionLogMarker, logEntry.toLogString());
    }

    private void doLog(
            final Exception exception,
            final HttpServletRequest request
    ) {
        final ExceptionLogEntry logEntry = ExceptionLogEntry.builder()
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .stackTrace(formatter.toStackTraceLog(exception))
                .httpMethod(request.getMethod())
                .requestUri(formatter.toUriWithQueryParamsLog(request))
                .ssaid(formatter.toSsaidLog(request))
                .build();
        final Marker exceptionLogMarker = MarkerFactory.getMarker("EXCEPTION-LOG");
        log.error(exceptionLogMarker, logEntry.toLogString());
    }
}
