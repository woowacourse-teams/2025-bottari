package com.bottari.alarm.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record LocationAlarm(
        boolean isLocationAlarmActive,
        Double latitude,
        Double longitude,
        int radius
) {
}
