package com.bottari.sse.component;

import com.bottari.error.ApiErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "SSE 관련 API")
public interface SseConnectorApiDocs {

    @Operation(summary = "팀 보따리 SSE 연결")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SSE 연결 성공(스트림 시작)"),
    })
    @ApiErrorCodes({
    })
    SseEmitter connectTeamBottari(
            final Long teamBottariId
    );
}
