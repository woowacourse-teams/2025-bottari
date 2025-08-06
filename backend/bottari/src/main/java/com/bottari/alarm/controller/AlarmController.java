package com.bottari.alarm.controller;

import com.bottari.alarm.service.AlarmService;
import com.bottari.alarm.dto.CreateAlarmRequest;
import com.bottari.alarm.dto.UpdateAlarmRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController implements AlarmApiDocs {

    private final AlarmService alarmService;

    @PostMapping("/bottaries/{bottariId}/alarms")
    @Override
    public ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateAlarmRequest request
    ) {
        final Long id = alarmService.create(bottariId, request);

        return ResponseEntity.created(URI.create("/alarms/" + id)).build();
    }

    @PutMapping("/alarms/{id}")
    @Override
    public ResponseEntity<Void> update(
            @PathVariable final Long id,
            @RequestBody final UpdateAlarmRequest request
    ) {
        alarmService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/alarms/{id}/active")
    @Override
    public ResponseEntity<Void> active(
            @PathVariable final Long id
    ) {
        alarmService.active(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/alarms/{id}/inactive")
    @Override
    public ResponseEntity<Void> inactive(
            @PathVariable final Long id
    ) {
        alarmService.inactive(id);

        return ResponseEntity.noContent().build();
    }
}
