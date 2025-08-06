package com.bottari.report.controller;

import com.bottari.bottaritemplate.dto.ReportBottariTemplateRequest;
import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ReportApiDocs {

    @Operation(summary = "보따리 템플릿을 신고")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 템플릿 신고 성공")
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.REPORT_ALREADY_EXISTS
    })
    ResponseEntity<Void> reportBottariTemplate(
            final Long id,
            final ReportBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    );
}
