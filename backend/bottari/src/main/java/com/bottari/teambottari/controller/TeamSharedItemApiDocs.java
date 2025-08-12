package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Shared Item", description = "팀 보따리 공통 아이템 API")
public interface TeamSharedItemApiDocs {

    @Operation(summary = "공통 물품 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공통 물품 체크 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_CHECKED
    })
    ResponseEntity<Void> check(
            final Long id,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "공통 물품 체크 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공통 물품 체크 해제 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED
    })
    ResponseEntity<Void> uncheck(
            final Long id,
            @Parameter(hidden = true) final String ssaid
    );
}
