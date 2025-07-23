package com.bottari.dto;

import com.bottari.domain.Alarm;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record AlarmResponse(
        Long id,
        RoutineAlarmResponse routine,
        LocationAlarmResponse location
) {

    public static AlarmResponse from(final Alarm alarm) {
        if (alarm == null) {
            return null;
        }

        return new AlarmResponse(
                alarm.getId(),
                RoutineAlarmResponse.from(alarm.getRoutineAlarm()),
                LocationAlarmResponse.from(alarm.getLocationAlarm())
        );
    }

    public record RoutineAlarmResponse(
            LocalTime time,
            RepeatType type,
            LocalDate date,
            List<Integer> dayOfWeeks
    ) {

        public static RoutineAlarmResponse from(final RoutineAlarm routineAlarm) {
            return new RoutineAlarmResponse(
                    routineAlarm.getTime(),
                    routineAlarm.getType(),
                    routineAlarm.getDate(),
                    convertDayOfWeeksToInteger(routineAlarm.repeatDayOfWeeks())
            );
        }

        private static List<Integer> convertDayOfWeeksToInteger(final Set<DayOfWeek> dayOfWeeks) {
            return dayOfWeeks.stream()
                    .map(DayOfWeek::getValue)
                    .toList();
        }
    }

    public record LocationAlarmResponse(
            boolean isActive,
            Double latitude,
            Double longitude,
            int radius
    ) {

        public static LocationAlarmResponse from(final LocationAlarm locationAlarm) {
            if (locationAlarm == null) {
                return null;
            }

            return new LocationAlarmResponse(
                    locationAlarm.isLocationAlarmActive(),
                    locationAlarm.latitude(),
                    locationAlarm.longitude(),
                    locationAlarm.radius()
            );
        }
    }
}
