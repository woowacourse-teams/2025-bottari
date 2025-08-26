package com.bottari.apiworkshop;

import java.time.LocalDate;
import java.util.List;

// TODO: 문서에서 pagination -> cursor로 변경
// TODO: lastId를 어떻게 다른 값으로 대체할 수 있을지 고민 및 문서 최신화
public record MostIncludedResponse(
    String query,
    LocalDate start,
    LocalDate end,
    Cursor cursor,
    List<ItemResponse> items
) {

    // TODO: nextCursor로 변경 후, 이를 인코딩
    record Cursor(
        int limit,
        String lastId,
        boolean hasNext,
        int lastRank,
        Long lastCount
    ) {
    }
}
