package com.bottari.apiworkshop.dto;

import com.bottari.apiworkshop.repository.ItemProjection;

public record ItemResponse(
        Long rank,
        String name,
        Long includedCount
) {

    public static ItemResponse of(
            final Long rank,
            final ItemProjection projection
    ) {
        return new ItemResponse(
                rank,
                projection.getName(),
                projection.getIncludedCount()
        );
    }
}
