package com.bottari.dto;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.LocationAlarm;
import com.bottari.domain.RepeatType;
import com.bottari.domain.RoutineAlarm;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record ReadBottariResponse(
        Long id,
        String title,
        List<BottariItemResponse> items,
        AlarmResponse alarm
) {

    public static ReadBottariResponse of(
            final Bottari bottari,
            final List<BottariItem> bottariItems,
            final Alarm alarm
    ) {
        return new ReadBottariResponse(
                bottari.getId(),
                bottari.getTitle(),
                bottariItems.stream().map(BottariItemResponse::from).toList(),
                AlarmResponse.from(alarm)
        );
    }

    public record BottariItemResponse(
            Long id,
            String name
    ) {

        public static BottariItemResponse from(final BottariItem bottariItem) {
            return new BottariItemResponse(
                    bottariItem.getId(),
                    bottariItem.getName()
            );
        }
    }

    public record AlarmResponse(
            RoutineAlarmResponse routine,
            LocationAlarmResponse location
    ) {

        public static AlarmResponse from(final Alarm alarm) {
            if (alarm == null) {
                return null;
            }

            return new AlarmResponse(
                    RoutineAlarmResponse.from(alarm.getRoutineAlarm()),
                    LocationAlarmResponse.from(alarm.getLocationAlarm())
            );
        }
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
            return new LocationAlarmResponse(
                    locationAlarm.isLocationAlarmActive(),
                    locationAlarm.latitude(),
                    locationAlarm.longitude(),
                    locationAlarm.radius()
            );
        }
    }
}
