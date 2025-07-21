package com.bottari.controller.docs;

import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.dto.ReadBottariItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Bottari Item", description = "보따리 아이템 API")
public interface BottariItemApiDocs {

    @Operation(summary = "보따리 체크리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 체크리스트 조회 성공"),
    })
    ResponseEntity<List<ReadBottariItemResponse>> readChecklist(
            @PathVariable final Long bottariId
    );

    @Operation(summary = "보따리 물품 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 물품 생성 성공"),
    })
    ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateBottariItemRequest request
    );

    @Operation(summary = "보따리 물품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 삭제 성공"),
    })
    ResponseEntity<Void> delete(
            @PathVariable final Long id
    );

    @Operation(summary = "보따리 물품 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 체크 성공"),
    })
    ResponseEntity<Void> check(
            @PathVariable final Long id
    );

    @Operation(summary = "보따리 물품 체크 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 체크 해제 성공"),
    })
    ResponseEntity<Void> uncheck(
            @PathVariable final Long id
    );
}
