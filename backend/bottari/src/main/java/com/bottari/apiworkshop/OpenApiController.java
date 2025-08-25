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
            // 정렬 기준(최신순/가져간 횟수)
            @RequestParam(defaultValue = "COUNT") final SortBy sortBy,
            // 마지막으로 가져간 아이템의 ID
            @RequestParam(required = false) final Long lastId,
            // 마지막 아이템의 순위
            @RequestParam(required = false) final int lastRank,
            // 마지막 아이템의 포함된 횟수
            @RequestParam(required = false) final int lastCount
    ) {
        MostIncludedResponse response = null;

        return ResponseEntity.ok(response);
    }
}
