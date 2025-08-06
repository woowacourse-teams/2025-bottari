package com.bottari.bottari.controller;

import com.bottari.bottari.service.BottariItemService;
import com.bottari.bottari.dto.CreateBottariItemRequest;
import com.bottari.bottari.dto.ReadBottariItemResponse;
import com.bottari.bottari.dto.EditBottariItemsRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BottariItemController implements BottariItemApiDocs {

    private final BottariItemService bottariItemService;

    @GetMapping("/bottaries/{bottariId}/bottari-items")
    @Override
    public ResponseEntity<List<ReadBottariItemResponse>> readChecklist(
            @PathVariable final Long bottariId
    ) {
        final List<ReadBottariItemResponse> responses = bottariItemService.getAllByBottariId(bottariId);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/bottaries/{bottariId}/bottari-items")
    @Override
    public ResponseEntity<Void> create(
            @PathVariable final Long bottariId,
            @RequestBody final CreateBottariItemRequest request
    ) {
        final Long id = bottariItemService.create(bottariId, request);

        return ResponseEntity.created(URI.create("/bottaries/" + bottariId + "/bottari-items/" + id)).build();
    }

    @PatchMapping("/bottaries/{bottariId}/bottari-items")
    @Override
    public ResponseEntity<Void> update(
            @PathVariable final Long bottariId,
            @RequestBody final EditBottariItemsRequest request
    ) {
        bottariItemService.update(bottariId, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bottari-items/{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable final Long id
    ) {
        bottariItemService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bottari-items/{id}/check")
    @Override
    public ResponseEntity<Void> check(
            @PathVariable final Long id
    ) {
        bottariItemService.check(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bottari-items/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id
    ) {
        bottariItemService.uncheck(id);

        return ResponseEntity.noContent().build();
    }
}
