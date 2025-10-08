package com.bottari.bottaritemplate.controller;

import com.bottari.bottaritemplate.dto.ReadHashtagWithUsageCountResponse;
import com.bottari.error.ApiErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Hashtag", description = "해시태그 관련 API")
public interface HashtagApiDocs {

    @Operation(
            summary = "인기 해시태그 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 해시태그 조회 성공")
            }
    )
    @ApiErrorCodes({
    })
    ResponseEntity<List<ReadHashtagWithUsageCountResponse>> readPopularHashtags(
            final int limit
    );
}
