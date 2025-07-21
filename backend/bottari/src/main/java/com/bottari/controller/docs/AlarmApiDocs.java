package com.bottari.controller.docs;

import com.bottari.dto.CreateAlarmRequest;
import com.bottari.dto.UpdateAlarmRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Alarm", description = "알람 API")
public interface AlarmApiDocs {

    @Operation(summary = "알람 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "알람 생성 성공"),
    })
    ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateAlarmRequest request
    );

    @Operation(summary = "알람 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 수정 성공"),
    })
    ResponseEntity<Void> update(
            @PathVariable final Long id,
            @RequestBody final UpdateAlarmRequest request
    );

    @Operation(summary = "알람 활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 활성화 성공"),
    })
    ResponseEntity<Void> active(
            @PathVariable final Long id
    );

    @Operation(summary = "알람 비활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "알람 비활성화 성공"),
    })
    ResponseEntity<Void> inactive(
            @PathVariable final Long id
    );
}
