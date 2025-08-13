package com.bottari.teambottari.dto;

import com.bottari.alarm.dto.AlarmResponse;

public record ReadTeamBottariPreviewResponse(
        Long id,
        String title,
        int totalItemsCount,
        int checkedItemsCount,
        int memberCount,
        AlarmResponse alarm
) {
}
