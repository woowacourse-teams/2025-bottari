package com.bottari.error;

import jakarta.servlet.http.HttpServletRequest;
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
        log.warn("Request URL: {} | Exception: {}", request.getRequestURI(), exception.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(
            final BusinessException exception,
            final HttpServletRequest request
    ) {
        log.warn("Request URL: {} | Exception: {}", request.getRequestURI(), exception.getMessage());
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
        log.error("Request URL: {} | Exception: {}", request.getRequestURI(), exception.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "예상치 못한 서버 에러입니다. 관리자(코기)를 찾아가세요."
        );
    }
}
