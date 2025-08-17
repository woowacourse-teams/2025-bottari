package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamPersonalItem;

public record ReadPersonalItemResponse(
        Long id,
        String name
) {

    public static ReadPersonalItemResponse from(final TeamPersonalItem teamPersonalItem) {
        return new ReadPersonalItemResponse(
                teamPersonalItem.getId(),
                teamPersonalItem.getName()
        );
    }
}
