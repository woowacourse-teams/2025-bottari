package com.bottari.dto;

import com.bottari.domain.LocationAlarm;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public record UpdateAlarmRequest(
        RoutineAlarmRequest routineAlarm,
        LocationAlarmRequest locationAlarm
) {

    public RoutineAlarm toRoutineAlarm() {
        return new RoutineAlarm(
                routineAlarm.time,
                routineAlarm.type,
                routineAlarm.date,
                routineAlarm.repeatDayOfWeekValues.stream()
                        .map(DayOfWeek::of)
                        .collect(Collectors.toSet())
        );
    }

    public LocationAlarm toLocationAlarm() {
        if (locationAlarm == null) {
            return null;
        }

        return new LocationAlarm(
                locationAlarm.isLocationAlarmActive,
                locationAlarm.latitude,
                locationAlarm.longitude,
                locationAlarm.radius
        );
    }

    public record RoutineAlarmRequest(
            LocalTime time,
            RepeatType type,
            LocalDate date,
            List<Integer> repeatDayOfWeekValues
    ) {
    }

    public record LocationAlarmRequest(
            boolean isLocationAlarmActive,
            Double latitude,
            Double longitude,
            int radius
    ) {
    }
}
