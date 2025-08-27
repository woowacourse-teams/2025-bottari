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
}
