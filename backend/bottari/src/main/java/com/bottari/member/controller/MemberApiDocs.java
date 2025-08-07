package com.bottari.member.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.member.dto.CheckRegistrationResponse;
import com.bottari.member.dto.CreateMemberRequest;
import com.bottari.member.dto.UpdateMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member", description = "사용자 API")
public interface MemberApiDocs {

    @Operation(summary = "사용자 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 등록 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_SSAID_ALREADY_EXISTS,
            ErrorCode.MEMBER_NAME_TOO_SHORT,
            ErrorCode.MEMBER_NAME_TOO_LONG
    })
    ResponseEntity<Void> register(
            final CreateMemberRequest request
    );

    @Operation(summary = "사용자 회원가입 여부 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 회원가입 여부 확인 성공"),
    })
    ResponseEntity<CheckRegistrationResponse> checkRegistration(
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "사용자 이름 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 이름 수정 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NAME_ALREADY_EXISTS,
            ErrorCode.MEMBER_NAME_UNCHANGED,
            ErrorCode.MEMBER_NAME_TOO_SHORT,
            ErrorCode.MEMBER_NAME_TOO_LONG
    })
    ResponseEntity<Void> updateName(
            final UpdateMemberRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
