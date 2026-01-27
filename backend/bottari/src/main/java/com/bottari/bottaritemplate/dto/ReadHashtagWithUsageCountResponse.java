package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;

public record ReadHashtagWithUsageCountResponse(
        Long id,
        String name,
        Long usageCount
) {

    public static ReadHashtagWithUsageCountResponse of(
            final HashtagPopularityProjection projection
    ) {
        return new ReadHashtagWithUsageCountResponse(
                projection.hashtagId(),
                projection.hashtagName(),
                projection.usageCount()
        );
    }
}
