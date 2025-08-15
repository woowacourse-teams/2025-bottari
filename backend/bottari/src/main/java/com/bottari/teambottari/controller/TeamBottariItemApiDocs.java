package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Bottari Item", description = "팀 보따리 물품 API")
public interface TeamBottariItemApiDocs {

    @Operation(summary = "팀 보따리 공통/담당 물품 조회", description = "각 멤버 별 체크 현황 포함 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공통/담당 물품 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<ReadTeamItemStatusResponse> readTeamItemsStatus(
            final Long teamBottariId,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 공통/담당 물품 보채기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보채기 알람 전송 성공")
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_ITEM_INAPPROPRIATE_TYPE,
            ErrorCode.TEAM_BOTTARI_ITEM_INFO_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<Void> sendRemindAlarmByItemInfo(
            final Long id,
            final TeamItemTypeRequest request,
            @Parameter(hidden = true) final String ssaid
    );

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
            final TeamItemTypeRequest request,
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
            final TeamItemTypeRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
