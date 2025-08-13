package com.bottari.fcm.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.dto.UpdateFcmRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Fcm Token", description = "Fcm Token API")
public interface FcmTokenApiDocs {

    @Operation(summary = "FCM 토큰 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FCM 토큰 업데이트 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.FCM_TOKEN_NOT_FOUND
    })
    ResponseEntity<Void> updateFcmToken(
            final UpdateFcmRequest request,
            @Parameter(hidden = true) final String ssaid
    );
}
