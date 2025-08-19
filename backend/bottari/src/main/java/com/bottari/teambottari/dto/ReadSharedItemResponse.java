package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamSharedItemInfo;

public record ReadSharedItemResponse(
        Long id,
        String name
) {

    public static ReadSharedItemResponse from(final TeamSharedItemInfo teamSharedItemInfo) {
        return new ReadSharedItemResponse(
                teamSharedItemInfo.getId(),
                teamSharedItemInfo.getName()
        );
    }
}
