package com.bottari.alarm.controller;

import com.bottari.alarm.dto.CreateAlarmRequest;
import com.bottari.alarm.dto.UpdateAlarmRequest;
import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Alarm", description = "알람 API")
public interface AlarmApiDocs {

    @Operation(summary = "알람 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "알람 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.ALARM_LOCATION_REQUIRES_ROUTINE
    })
    ResponseEntity<Void> create(
            final Long bottariId,
            final CreateAlarmRequest request
    );

    @Operation(summary = "알람 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 수정 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.ALARM_NOT_FOUND,
            ErrorCode.ALARM_LOCATION_REQUIRES_ROUTINE
    })
    ResponseEntity<Void> update(
            final Long id,
            final UpdateAlarmRequest request
    );

    @Operation(summary = "알람 활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 활성화 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.ALARM_NOT_FOUND,
            ErrorCode.ALARM_ALREADY_ACTIVE
    })
    ResponseEntity<Void> active(
            final Long id
    );

    @Operation(summary = "알람 비활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 비활성화 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.ALARM_NOT_FOUND,
            ErrorCode.ALARM_ALREADY_INACTIVE
    })
    ResponseEntity<Void> inactive(
            final Long id
    );
}
