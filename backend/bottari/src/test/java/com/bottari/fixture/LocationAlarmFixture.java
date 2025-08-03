package com.bottari.fixture;

import com.bottari.domain.LocationAlarm;

public enum LocationAlarmFixture {

    LOCATION_ALARM_ON(true, 37.5665, 126.9780, 100),
    LOCATION_ALARM_OFF(false, null, null, 0);

    private final boolean isLocationAlarmActive;
    private final Double latitude;
    private final Double longitude;
    private final int radius;

    LocationAlarmFixture(
            final boolean isLocationAlarmActive,
            final Double latitude,
            final Double longitude,
            final int radius
    ) {
        this.isLocationAlarmActive = isLocationAlarmActive;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public LocationAlarm getLocationAlarm() {
        return new LocationAlarm(isLocationAlarmActive, latitude, longitude, radius);
    }
}
