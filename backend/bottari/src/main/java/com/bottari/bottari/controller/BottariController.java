package com.bottari.bottari.controller;

import com.bottari.bottari.dto.CreateBottariRequest;
import com.bottari.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.bottari.dto.ReadBottariResponse;
import com.bottari.bottari.dto.UpdateBottariRequest;
import com.bottari.bottari.service.BottariService;
import com.bottari.config.MemberIdentifier;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bottaries")
@RequiredArgsConstructor
public class BottariController implements BottariApiDocs {

    private final BottariService bottariService;

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ReadBottariResponse> read(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        final ReadBottariResponse response = bottariService.getById(ssaid, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ReadBottariPreviewResponse>> readPreviews(
            @MemberIdentifier final String ssaid
    ) {
        final List<ReadBottariPreviewResponse> responses = bottariService.getAllBySsaidSortedByLatest(ssaid);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Override
    public ResponseEntity<Void> create(
            @RequestBody final CreateBottariRequest request,
            @MemberIdentifier final String ssaid
    ) {
        final Long id = bottariService.create(ssaid, request);

        return ResponseEntity.created(URI.create("/bottaries/" + id)).build();
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Void> update(
            @PathVariable final Long id,
            @RequestBody final UpdateBottariRequest request,
            @MemberIdentifier final String ssaid
    ) {
        bottariService.update(request, id, ssaid);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        bottariService.deleteById(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
