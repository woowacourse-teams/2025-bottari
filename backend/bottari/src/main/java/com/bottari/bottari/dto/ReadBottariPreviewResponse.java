package com.bottari.bottari.dto;

import com.bottari.alarm.dto.AlarmResponse;

public record ReadBottariPreviewResponse(
        Long id,
        String title,
        int totalItemsCount,
        int checkedItemsCount,
        AlarmResponse alarm
) {
}
