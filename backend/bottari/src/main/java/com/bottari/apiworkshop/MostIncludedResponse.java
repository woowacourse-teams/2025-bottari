package com.bottari.apiworkshop;

import java.time.LocalDate;
import java.util.List;

public record MostIncludedResponse(
    String query,
    LocalDate start,
    LocalDate end,
    Cursor cursor,
    List<ItemResponse> items
) {

    public static MostIncludedResponse empty(
            final String query,
            final LocalDate start,
            final LocalDate end
    ) {
        return new MostIncludedResponse(
            query,
            start,
            end,
            new Cursor(0, null, false, 0, null),
            List.of()
        );
    }
}
