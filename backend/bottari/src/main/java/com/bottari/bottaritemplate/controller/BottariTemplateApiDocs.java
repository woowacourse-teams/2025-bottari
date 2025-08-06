package com.bottari.bottaritemplate.controller;

import com.bottari.bottaritemplate.dto.CreateBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadBottariTemplateResponse;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateResponse;
import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Bottari Template", description = "보따리 템플릿 API")
public interface BottariTemplateApiDocs {

    @Operation(summary = "보따리 템플릿 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 템플릿 상세 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND
    })
    ResponseEntity<ReadBottariTemplateResponse> read(
            final Long id
    );

    @Operation(summary = "내 보따리 템플릿 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 보따리 템플릿 목록 조회 성공")
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND
    })
    ResponseEntity<List<ReadBottariTemplateResponse>> readMine(
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "보따리 템플릿 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 템플릿 목록 조회 성공"),
    })
    ResponseEntity<List<ReadBottariTemplateResponse>> readAll(
            final String query
    );

    @Operation(summary = "커서 기반 보따리 템플릿 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커서 기반 보따리 템플릿 목록 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.DATE_FORMAT_INVALID,
            ErrorCode.NUMBER_FORMAT_INVALID,
            ErrorCode.BOTTARI_TEMPLATE_INVALID_SORT_TYPE
    })
    ResponseEntity<ReadNextBottariTemplateResponse> readNextAll(
            final ReadNextBottariTemplateRequest request
    );

    @Operation(summary = "보따리 템플릿 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 템플릿 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.BOTTARI_TEMPLATE_TITLE_BLANK,
            ErrorCode.BOTTARI_TEMPLATE_TITLE_TOO_LONG,
            ErrorCode.BOTTARI_TEMPLATE_ITEM_DUPLICATE_IN_REQUEST,
            ErrorCode.BOTTARI_TEMPLATE_ITEM_NAME_BLANK,
            ErrorCode.BOTTARI_TEMPLATE_ITEM_NAME_TOO_LONG
    })
    ResponseEntity<Void> create(
            final CreateBottariTemplateRequest request,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "보따리 템플릿으로 보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 템플릿으로 보따리 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND,
            ErrorCode.BOTTARI_TITLE_BLANK,
            ErrorCode.BOTTARI_TITLE_TOO_LONG,
            ErrorCode.BOTTARI_ITEM_NAME_BLANK,
            ErrorCode.BOTTARI_ITEM_NAME_TOO_LONG
    })
    ResponseEntity<Void> createBottari(
            final Long id,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "보따리 템플릿 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 템플릿 삭제 성공")
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND,
            ErrorCode.BOTTARI_TEMPLATE_NOT_OWNED
    })
    ResponseEntity<Void> delete(
            final Long id,
            @Parameter(hidden = true) final String ssaid
    );
}
