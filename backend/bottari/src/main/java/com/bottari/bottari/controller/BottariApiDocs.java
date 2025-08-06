package com.bottari.bottari.controller;

import com.bottari.bottari.dto.CreateBottariRequest;
import com.bottari.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.bottari.dto.ReadBottariResponse;
import com.bottari.bottari.dto.UpdateBottariRequest;
import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Bottari", description = "보따리 API")
public interface BottariApiDocs {

    @Operation(summary = "보따리를 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 상세 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.BOTTARI_NOT_OWNED
    })
    ResponseEntity<ReadBottariResponse> read(
            final Long id,
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "내 보따리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 보따리 목록 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND
    })
    ResponseEntity<List<ReadBottariPreviewResponse>> readPreviews(
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.BOTTARI_TITLE_BLANK,
            ErrorCode.BOTTARI_TITLE_TOO_LONG
    })
    ResponseEntity<Void> create(
            final CreateBottariRequest request,
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "보따리 이름 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 이름 수정 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.BOTTARI_NOT_OWNED,
            ErrorCode.BOTTARI_TITLE_UNCHANGED,
            ErrorCode.BOTTARI_TITLE_BLANK,
            ErrorCode.BOTTARI_TITLE_TOO_LONG
    })
    ResponseEntity<Void> update(
            final Long id,
            final UpdateBottariRequest request,
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "보따리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 삭제 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.BOTTARI_NOT_OWNED
    })
    ResponseEntity<Void> delete(
            final Long id,
            final HttpServletRequest httpServletRequest
    );
}
