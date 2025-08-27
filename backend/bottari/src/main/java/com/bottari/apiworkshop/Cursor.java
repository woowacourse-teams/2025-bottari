package com.bottari.apiworkshop;

// TODO: nextCursor로 변경 후, 이를 인코딩
record Cursor(
        int limit,
        String lastId,
        boolean hasNext,
        int lastRank,
        Long lastCount
) {
}
