package com.bottari.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health Check", description = "헬스 체크 API")
public interface HealthCheckApiDocs {

    @Operation(summary = "서버 상태 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 상태 양호"),
    })
    ResponseEntity<Void> healthCheck();
}
