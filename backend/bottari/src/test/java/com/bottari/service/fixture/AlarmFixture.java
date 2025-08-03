package com.bottari.service.fixture;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.RoutineAlarm;

public enum AlarmFixture {

    ALARM_ON(true),
    ALARM_OFF(false);

    private final boolean isActive;

    AlarmFixture(final boolean isActive) {
        this.isActive = isActive;
    }

    public Alarm getAlarm(
            final RoutineAlarm routineAlarm,
            final LocationAlarm locationAlarm,
            final Bottari bottari
    ) {
        return new Alarm(isActive, routineAlarm, locationAlarm, bottari);
    }
}
