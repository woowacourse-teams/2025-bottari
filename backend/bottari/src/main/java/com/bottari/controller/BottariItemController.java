package com.bottari.controller;

import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.service.BottariItemService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BottariItemController {

    private final BottariItemService bottariItemService;

    @PostMapping("/bottaries/{bottariId}/bottari-items")
    public ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateBottariItemRequest request
    ) {
        final Long id = bottariItemService.create(bottariId, request);

        return ResponseEntity.created(URI.create("/bottaries/" + bottariId + "/bottari-items/" + id)).build();
    }

    @DeleteMapping("/bottari-items/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable final Long id
    ) {
        bottariItemService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bottari-items/{id}/check")
    public ResponseEntity<Void> check(
            @PathVariable final Long id
    ) {
        bottariItemService.check(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bottari-items/{id}/uncheck")
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id
    ) {
        bottariItemService.uncheck(id);

        return ResponseEntity.noContent().build();
    }
}
