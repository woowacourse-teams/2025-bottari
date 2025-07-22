package com.bottari.controller.docs;

import com.bottari.dto.CreateBottariTemplateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "Bottari Template", description = "보따리 템플릿 API")
public interface BottariTemplateApiDocs {

    @Operation(summary = "보따리 템플릿 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 템플릿 생성 성공"),
    })
    ResponseEntity<Void> create(
            final CreateBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    );
}
