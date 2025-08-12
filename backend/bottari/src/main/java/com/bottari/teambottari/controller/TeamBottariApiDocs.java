package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface TeamBottariApiDocs {

    @Operation(summary = "보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팀 보따리 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.TEAM_BOTTARI_ITEM_NAME_BLANK,
            ErrorCode.TEAM_BOTTARI_TITLE_TOO_LONG,
            ErrorCode.TEAM_BOTTARI_INVITE_CODE_GENERATION_FAILED
    })
    ResponseEntity<Void> create(
            final CreateTeamBottariRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
