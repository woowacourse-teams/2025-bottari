package com.bottari.controller;

import com.bottari.dto.CreateAlarmRequest;
import com.bottari.dto.UpdateAlarmRequest;
import com.bottari.service.AlarmService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/bottaries/{bottariId}/alarms")
    public ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateAlarmRequest request
    ) {
        final Long id = alarmService.create(bottariId, request);

        return ResponseEntity.created(URI.create("/alarms/" + id)).build();
    }

    @PutMapping("/alarms/{id}")
    public ResponseEntity<Void> update(
            @PathVariable final Long id,
            @RequestBody final UpdateAlarmRequest request
    ) {
        alarmService.update(id, request);

        return ResponseEntity.noContent().build();
    }
}
