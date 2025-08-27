package com.bottari.apiworkshop;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record MostIncludedQuery(
        String query,
        @DateTimeFormat(iso = ISO.DATE) LocalDate start,
        @DateTimeFormat(iso = ISO.DATE) LocalDate end
) {
}
