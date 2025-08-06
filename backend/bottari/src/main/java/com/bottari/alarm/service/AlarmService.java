package com.bottari.alarm.service;

import com.bottari.alarm.domain.Alarm;
import com.bottari.alarm.repository.AlarmRepository;
import com.bottari.bottari.domain.Bottari;
import com.bottari.alarm.dto.CreateAlarmRequest;
import com.bottari.alarm.dto.UpdateAlarmRequest;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.bottari.repository.BottariRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final BottariRepository bottariRepository;

    @Transactional
    public Long create(
            final Long bottariId,
            final CreateAlarmRequest request
    ) {
        final Bottari bottari = bottariRepository.findById(bottariId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
        final Alarm alarm = new Alarm(
                true,
                request.toRoutineAlarm(),
                request.toLocationAlarm(),
                bottari
        );
        final Alarm savedAlarm = alarmRepository.save(alarm);

        return savedAlarm.getId();
    }

    @Transactional
    public void update(
            final Long id,
            final UpdateAlarmRequest request
    ) {
        final Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_NOT_FOUND));
        alarm.update(request.toRoutineAlarm(), request.toLocationAlarm());
    }

    @Transactional
    public void active(final Long id) {
        final Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_NOT_FOUND));
        alarm.active();
    }

    @Transactional
    public void inactive(final Long id) {
        final Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_NOT_FOUND));
        alarm.inactive();
    }
}
