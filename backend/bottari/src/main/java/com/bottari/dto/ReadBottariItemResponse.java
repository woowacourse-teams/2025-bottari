package com.bottari.dto;

import com.bottari.domain.BottariItem;

public record ReadBottariItemResponse(
        Long id,
        String name,
        boolean isChecked
) {

    public static ReadBottariItemResponse from(final BottariItem bottariItem) {
        return new ReadBottariItemResponse(
                bottariItem.getId(),
                bottariItem.getName(),
                bottariItem.isChecked()
        );
    }
}
