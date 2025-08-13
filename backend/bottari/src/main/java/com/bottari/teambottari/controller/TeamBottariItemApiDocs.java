package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Bottari Item", description = "팀 보따리 물품 API")
public interface TeamBottariItemApiDocs {

    @Operation(summary = "팀 보따리 체크리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "체크 리스트 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<TeamMemberChecklistResponse> readChecklistBySsaid(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 물품 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팀 보따리 물품 물품 체크 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_CHECKED
    })
    ResponseEntity<Void> check(
            final Long id,
            final CheckTeamItemRequest request,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 물품 체크 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팀 보따리 물품 체크 해제 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED
    })
    ResponseEntity<Void> uncheck(
            final Long id,
            final CheckTeamItemRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
