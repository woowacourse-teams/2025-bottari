package com.bottari.apiworkshop;

record Cursor(
        int limit,
        String lastName,
        boolean hasNext,
        int lastRank,
        Long lastCount
) {
}
