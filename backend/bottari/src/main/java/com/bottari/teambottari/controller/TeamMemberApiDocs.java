package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Bottari", description = "팀 보따리 API")
public interface TeamMemberApiDocs {

    @Operation(summary = "팀 보따리 팀원 관리 정보 조회 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 보따리 팀원 관리 정보 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<ReadTeamMemberInfoResponse> readTeamMemberManagementInfo(
            final Long teamBottariId,
            final String ssaid
    );
}
