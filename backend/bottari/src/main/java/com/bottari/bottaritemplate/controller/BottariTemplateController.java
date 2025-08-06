package com.bottari.bottaritemplate.controller;

import com.bottari.bottaritemplate.dto.CreateBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadBottariTemplateResponse;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateResponse;
import com.bottari.bottaritemplate.service.BottariTemplateService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class BottariTemplateController implements BottariTemplateApiDocs {

    private final BottariTemplateService bottariTemplateService;

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ReadBottariTemplateResponse> read(
            @PathVariable final Long id
    ) {
        final ReadBottariTemplateResponse response = bottariTemplateService.getById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Override
    public ResponseEntity<List<ReadBottariTemplateResponse>> readMine(
            @RequestHeader("ssaid") final String ssaid
    ) {
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getBySsaid(ssaid);

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ReadBottariTemplateResponse>> readAll(
            @RequestParam(required = false, defaultValue = "") final String query
    ) {
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getAll(query);

        return ResponseEntity.ok(responses);
    }

    // TODO: 연동 후, readAll로 대치
    @GetMapping("/cursor")
    @Override
    public ResponseEntity<ReadNextBottariTemplateResponse> readNextAll(
            @ModelAttribute final ReadNextBottariTemplateRequest request
    ) {
        final ReadNextBottariTemplateResponse response = bottariTemplateService.getNextAll(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<Void> create(
            @RequestBody final CreateBottariTemplateRequest request,
            @RequestHeader("ssaid") final String ssaid
    ) {
        final Long id = bottariTemplateService.create(ssaid, request);

        return ResponseEntity.created(URI.create("/templates/" + id)).build();
    }

    @PostMapping("/{id}/create-bottari")
    @Override
    public ResponseEntity<Void> createBottari(
            @PathVariable final Long id,
            @RequestHeader("ssaid") final String ssaid
    ) {
        final Long bottariId = bottariTemplateService.createBottari(id, ssaid);

        return ResponseEntity.created(URI.create("/bottaries/" + bottariId)).build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable final Long id,
            @RequestHeader("ssaid") final String ssaid
    ) {
        bottariTemplateService.deleteById(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
