package com.bottari.controller;

import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.service.BottariItemService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bottaries/{bottariId}/items")
@RequiredArgsConstructor
public class BottariItemController {

    private final BottariItemService bottariItemService;

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateBottariItemRequest request
    ) {
        final Long id = bottariItemService.create(bottariId, request);

        return ResponseEntity.created(URI.create("/bottaries/" + bottariId + "/items/" + id)).build();
    }
}
