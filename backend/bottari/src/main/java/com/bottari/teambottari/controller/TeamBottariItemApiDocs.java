package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "TeamBottariItem", description = "팀 보따리 물품 API")
public interface TeamBottariItemApiDocs {

    @Operation(summary = "팀 보따리 체크리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "체크 리스트 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.TEAM_MEMBER_NOT_FOUND
    })
    ResponseEntity<TeamMemberChecklistResponse> readChecklistBySsaid(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );
}
