package com.bottari.fixture;

import com.bottari.alarm.domain.Alarm;
import com.bottari.alarm.domain.LocationAlarm;
import com.bottari.alarm.domain.RoutineAlarm;
import com.bottari.bottari.domain.Bottari;

public enum AlarmFixture {

    ALARM_ON(true),
    ALARM_OFF(false),
    ;

    private final boolean isActive;

    AlarmFixture(final boolean isActive) {
        this.isActive = isActive;
    }

    public Alarm get(
            final RoutineAlarm routineAlarm,
            final LocationAlarm locationAlarm,
            final Bottari bottari
    ) {
        return new Alarm(isActive, routineAlarm, locationAlarm, bottari);
    }
}
