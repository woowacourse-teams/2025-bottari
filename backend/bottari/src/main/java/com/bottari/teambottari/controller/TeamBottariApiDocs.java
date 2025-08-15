package com.bottari.teambottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Team Bottari", description = "팀 보따리 API")
public interface TeamBottariApiDocs {

    @Operation(summary = "내가 소속된 팀 보따리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 소속된 팀 보따리 목록 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND
    })
    ResponseEntity<List<ReadTeamBottariPreviewResponse>> readPreviews(
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 보따리 상세 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.TEAM_BOTTARI_NOT_FOUND,
            ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI
    })
    ResponseEntity<ReadTeamBottariResponse> read(
            final Long id,
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "팀 보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팀 보따리 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.ITEM_NAME_BLANK,
            ErrorCode.BOTTARI_TITLE_TOO_LONG,
            ErrorCode.TEAM_BOTTARI_INVITE_CODE_GENERATION_FAILED
    })
    ResponseEntity<Void> create(
            final CreateTeamBottariRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
