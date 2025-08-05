package com.bottari.dto;

public record ReadBottariPreviewResponse(
        Long id,
        String title,
        int totalItemsCount,
        int checkedItemsCount,
        AlarmResponse alarm
) {
}
