package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;

public record ReadPopularHashtagResponse(
        Long id,
        String name,
        int ranking,
        int usageCount
) {

    public static ReadPopularHashtagResponse of(
            final int ranking,
            final HashtagPopularityProjection projection
    ) {
        return new ReadPopularHashtagResponse(
                projection.getHashtagId(),
                projection.getHashtagName(),
                ranking,
                projection.getUsageCount()
        );
    }
}
