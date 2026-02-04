package com.bottari.bottari.controller;

import com.bottari.error.ApiErrorCodes;
import com.bottari.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Bottari Backup", description = "나의 보따리 백업 API")
public interface BottariBackupApiDocs {

    @Operation(summary = "나의 보따리 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 보따리 불러오기 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_LOAD_FAILED
    })
    ResponseEntity<Resource> load(
            @Parameter(hidden = true) final String ssaid
    );

    @Operation(summary = "나의 보따리 백업")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 보따리 백업 성공"),
    })
    @ApiErrorCodes({
            ErrorCode.BOTTARI_BACKUP_FAILED
    })
    ResponseEntity<Map<?, ?>> backup(
            final MultipartFile file,
            @Parameter(hidden = true) final String ssaid
    );
}
