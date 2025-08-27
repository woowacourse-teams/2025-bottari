package com.bottari.apiworkshop.controller;

import com.bottari.apiworkshop.dto.CursorRequest;
import com.bottari.apiworkshop.dto.MostIncludedQuery;
import com.bottari.apiworkshop.dto.MostIncludedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Open API", description = "공공 API")
public interface OpenApiDocs {

    @Operation(summary = "템플릿 내 물품 포함횟수 순 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "템플릿 내 물품 포함횟수 순 조회 성공"),
    })
    ResponseEntity<MostIncludedResponse> mostIncluded(
            final MostIncludedQuery query,
            final CursorRequest cursor
    );
}
