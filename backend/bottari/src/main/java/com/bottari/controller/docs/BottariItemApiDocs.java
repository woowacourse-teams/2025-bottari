package com.bottari.controller.docs;

import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.dto.EditBottariItemsRequest;
import com.bottari.dto.ReadBottariItemResponse;
import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Bottari Item", description = "보따리 아이템 API")
public interface BottariItemApiDocs {

    @Operation(summary = "보따리 체크리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보따리 체크리스트 조회 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND
    })
    ResponseEntity<List<ReadBottariItemResponse>> readChecklist(
            final Long bottariId
    );

    @Operation(summary = "보따리 물품 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보따리 물품 생성 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.BOTTARI_ITEM_ALREADY_EXISTS,
            ErrorCode.BOTTARI_ITEM_NAME_BLANK,
            ErrorCode.BOTTARI_ITEM_NAME_TOO_LONG
    })
    ResponseEntity<Void> create(
            final Long bottariId,
            final CreateBottariItemRequest request
    );

    @Operation(summary = "보따리 물품 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 수정 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_NOT_FOUND,
            ErrorCode.BOTTARI_ITEM_NOT_IN_BOTTARI,
            ErrorCode.BOTTARI_ITEM_MAXIMUM_EXCEEDED,
            ErrorCode.BOTTARI_ITEM_DUPLICATE_IN_REQUEST,
            ErrorCode.BOTTARI_ITEM_ALREADY_EXISTS,
            ErrorCode.BOTTARI_ITEM_NAME_BLANK,
            ErrorCode.BOTTARI_ITEM_NAME_TOO_LONG
    })
    ResponseEntity<Void> update(
            final Long bottariId,
            final EditBottariItemsRequest request
    );

    @Operation(summary = "보따리 물품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 삭제 성공"),
    })
    ResponseEntity<Void> delete(
            final Long id
    );

    @Operation(summary = "보따리 물품 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 체크 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.BOTTARI_ITEM_ALREADY_CHECKED
    })
    ResponseEntity<Void> check(
            final Long id
    );

    @Operation(summary = "보따리 물품 체크 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "보따리 물품 체크 해제 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_ITEM_NOT_FOUND,
            ErrorCode.BOTTARI_ITEM_ALREADY_UNCHECKED
    })
    ResponseEntity<Void> uncheck(
            final Long id
    );
}
