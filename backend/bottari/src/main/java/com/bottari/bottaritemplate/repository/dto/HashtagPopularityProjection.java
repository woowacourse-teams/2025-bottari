package com.bottari.bottaritemplate.repository.dto;

public record HashtagPopularityProjection(
        Long hashtagId,
        String hashtagName,
        Long usageCount
) {
}
