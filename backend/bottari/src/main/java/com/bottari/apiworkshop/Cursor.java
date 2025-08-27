package com.bottari.apiworkshop;

public record Cursor(
        Long limit,
        String lastName,
        boolean hasNext,
        Long lastRank,
        Long lastCount
) {
}
