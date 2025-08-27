package com.bottari.apiworkshop.dto;

public record CursorRequest(
        Long limit,
        String lastName,
        Long lastRank,
        Long lastCount
) {

    public CursorRequest {
        limit = limit == null ? 10L : limit;
        lastRank = lastRank == null ? 0L : lastRank;
    }
}
