package com.bottari.apiworkshop.dto;

import java.util.List;

public record MostIncludedResponse(
    MostIncludedQuery query,
    Cursor cursor,
    List<ItemResponse> items
) {

    public static MostIncludedResponse empty(
            final MostIncludedQuery query
    ) {
        return new MostIncludedResponse(
                query,
                new Cursor(0L, null, false, 0L, null),
                List.of()
        );
    }

    public static MostIncludedResponse of(
            final MostIncludedQuery query,
            final Cursor cursor,
            final List<ItemResponse> itemResponses
    ) {
        return new MostIncludedResponse(
                query,
                cursor,
                itemResponses
        );
    }
}
