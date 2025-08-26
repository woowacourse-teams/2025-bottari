package com.bottari.apiworkshop;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

    private final OpenApiService openApiService;

    // TODO: limit에 대한 검증을 어디서 할지 생각해보기. (min/max)
    // TODO: endpoint 설계 다시 한 번 생각해보기(endpoint 명, query param 명, Pageable 적용 등)
    @GetMapping("/templates/most-included")
    public ResponseEntity<MostIncludedResponse> mostIncluded(
            // 상황/검색어
            @RequestParam(required = false) final String query,
            // 검색 범위 시작 날짜
            @RequestParam(required = false)
            @DateTimeFormat(iso = ISO.DATE) final LocalDate start,
            // 검색 범위 종료 날짜
            @RequestParam(required = false)
            @DateTimeFormat(iso = ISO.DATE) final LocalDate end,
            // 갯수
            @RequestParam(defaultValue = "10") final int limit,
            // 마지막으로 가져간 아이템의 물품명
            @RequestParam(required = false) final String lastName,
            // 마지막 아이템의 순위
            @RequestParam(defaultValue = "0") final int lastRank,
            // 마지막 아이템의 포함된 횟수
            @RequestParam(required = false) final Long lastCount
    ) {
        MostIncludedResponse response = openApiService.mostIncluded(
                query,
                start,
                end,
                limit,
                lastName,
                lastRank,
                lastCount
        );

        return ResponseEntity.ok(response);
    }
}
