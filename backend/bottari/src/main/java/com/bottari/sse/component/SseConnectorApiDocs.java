package com.bottari.sse.component;

import com.bottari.error.ApiErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "SSE 관련 API")
public interface SseConnectorApiDocs {

    @Operation(
            summary = "팀 보따리 SSE 연결",
            parameters = {
                    @Parameter(
                            name = HttpHeaders.ACCEPT,
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "SSE 스트림 연결을 위해 'text/event-stream' 값을 명시해야 합니다.",
                            schema = @Schema(type = "string", defaultValue = "text/event-stream")
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SSE 연결 성공(스트림 시작)"),
    })
    @ApiErrorCodes({
    })
    SseEmitter connectTeamBottari(
            final Long teamBottariId
    );
}
