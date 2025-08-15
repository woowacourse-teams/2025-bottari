package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberNameResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Member", description = "팀 멤버 API")
public interface TeamMemberApiDocs {

    @Operation(summary = "팀 보따리 팀원 관리 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 보따리 팀원 관리 정보 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<ReadTeamMemberInfoResponse> readTeamMemberManagementInfo(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 팀원 이름 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 보따리 팀원 이름 정보 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<List<ReadTeamMemberNameResponse>> readTeamMemberNames(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 멤버 별 챙김 현황 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 보따리 멤버 별 챙김 현황 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<List<ReadTeamMemberStatusResponse>> readTeamMemberStatus(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );
}
