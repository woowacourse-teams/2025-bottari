package com.bottari.alarm.dto;

import com.bottari.alarm.domain.LocationAlarm;
import com.bottari.alarm.domain.RoutineAlarm;
import com.bottari.alarm.domain.RepeatType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateAlarmRequest(
        RoutineAlarmRequest routineAlarm,
        LocationAlarmRequest locationAlarm
) {

    public RoutineAlarm toRoutineAlarm() {
        final Set<DayOfWeek> dayOfWeeks = routineAlarm.repeatDayOfWeekValues.stream()
                .map(DayOfWeek::of)
                .collect(Collectors.toSet());

        return new RoutineAlarm(
                routineAlarm.time,
                routineAlarm.type,
                routineAlarm.date,
                dayOfWeeks
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
