package com.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive;

    @Embedded
    private RoutineAlarm routineAlarm;

    @Embedded
    private LocationAlarm locationAlarm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottari_id")
    private Bottari bottari;

    public Alarm(
            final boolean isActive,
            final RoutineAlarm routineAlarm,
            final LocationAlarm locationAlarm,
            final Bottari bottari
    ) {
        validateLocationAlarm(routineAlarm, locationAlarm);
        this.isActive = isActive;
        this.routineAlarm = routineAlarm;
        this.locationAlarm = locationAlarm;
        this.bottari = bottari;
    }

    public void update(
            final RoutineAlarm routineAlarm,
            final LocationAlarm locationAlarm
    ) {
        validateLocationAlarm(routineAlarm, locationAlarm);
        this.routineAlarm = routineAlarm;
        this.locationAlarm = locationAlarm;
    }

    public void active() {
        if (isActive) {
            throw new BusinessException(ErrorCode.ALARM_ALREADY_ACTIVE);
        }
        this.isActive = true;
    }

    public void inactive() {
        if (!isActive) {
            throw new BusinessException(ErrorCode.ALARM_ALREADY_INACTIVE);
        }
        this.isActive = false;
    }

    private void validateLocationAlarm(
            final RoutineAlarm routineAlarm,
            final LocationAlarm locationAlarm
    ) {
        if (routineAlarm == null && locationAlarm != null) {
            throw new BusinessException(ErrorCode.ALARM_LOCATION_REQUIRES_ROUTINE);
        }
    }
}
