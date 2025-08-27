package com.bottari.apiworkshop.dto;

public record Cursor(
        Long limit,
        String lastName,
        boolean hasNext,
        Long lastRank,
        Long lastCount
) {
}
