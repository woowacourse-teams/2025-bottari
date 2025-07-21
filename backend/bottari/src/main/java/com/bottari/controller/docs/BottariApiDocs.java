package com.bottari.controller.docs;

import com.bottari.dto.CreateBottariRequest;
import com.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.dto.ReadBottariResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Bottari", description = "보따리 API")
public interface BottariApiDocs {

    @Operation(summary = "보따리를 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 상세 조회 성공"),
    })
    ResponseEntity<ReadBottariResponse> read(
            @PathVariable final Long id,
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "내 보따리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 보따리 목록 조회 성공"),
    })
    ResponseEntity<List<ReadBottariPreviewResponse>> readPreviews(
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 생성 성공"),
    })
    ResponseEntity<Void> create(
            @RequestBody final CreateBottariRequest request,
            final HttpServletRequest httpServletRequest
    );
}
