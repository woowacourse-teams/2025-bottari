package com.bottari.controller.docs;

import com.bottari.dto.CreateMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "사용자 API")
public interface MemberApiDocs {

    @Operation(summary = "사용자 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 등록 성공"),
    })
    ResponseEntity<Void> register(
            @RequestBody final CreateMemberRequest request
    );
}
