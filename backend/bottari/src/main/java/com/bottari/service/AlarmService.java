package com.bottari.service;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.dto.CreateAlarmRequest;
import com.bottari.repository.AlarmRepository;
import com.bottari.repository.BottariRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보따리입니다."));
        final Alarm alarm = new Alarm(
                true,
                request.toRoutineAlarm(),
                request.toLocationAlarm(),
                bottari
        );
        final Alarm savedAlarm = alarmRepository.save(alarm);

        return savedAlarm.getId();
    }
}
