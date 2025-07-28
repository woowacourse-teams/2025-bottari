package com.bottari.controller.docs;

import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Bottari Template", description = "보따리 템플릿 API")
public interface BottariTemplateApiDocs {

    @Operation(summary = "보따리 템플릿 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 템플릿 상세 조회 성공"),
    })
    ResponseEntity<ReadBottariTemplateResponse> read(final Long id);

    @Operation(summary = "내 보따리 템플릿 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 보따리 템플릿 목록 조회 성공")
    })
    ResponseEntity<List<ReadBottariTemplateResponse>> readMine(final HttpServletRequest httpServletRequest);

    @Operation(summary = "보따리 템플릿 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 템플릿 목록 조회 성공"),
    })
    ResponseEntity<List<ReadBottariTemplateResponse>> readAll(final String query);

    @Operation(summary = "보따리 템플릿 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 템플릿 생성 성공"),
    })
    ResponseEntity<Void> create(
            final CreateBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    );

    @Operation(summary = "보따리 템플릿으로 보따리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 템플릿으로 보따리 생성 성공"),
    })
    ResponseEntity<Void> createBottari(
            final Long id,
            final HttpServletRequest httpServletRequest
    );
}
