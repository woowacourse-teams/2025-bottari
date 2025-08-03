package com.bottari.controller;

import com.bottari.controller.docs.BottariTemplateApiDocs;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.dto.ReportBottariTemplateRequest;
import com.bottari.service.BottariTemplateService;
import com.bottari.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class BottariTemplateController implements BottariTemplateApiDocs {

    private final BottariTemplateService bottariTemplateService;
    private final ReportService reportService;

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
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getBySsaid(ssaid);

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ReadBottariTemplateResponse>> readAll(
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getAll(query);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Override
    public ResponseEntity<Void> create(
            @RequestBody final CreateBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        final Long id = bottariTemplateService.create(ssaid, request);

        return ResponseEntity.created(URI.create("/templates/" + id)).build();
    }

    @PostMapping("/{id}/create-bottari")
    @Override
    public ResponseEntity<Void> createBottari(
            @PathVariable final Long id,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        final Long bottariId = bottariTemplateService.createBottari(id, ssaid);

        return ResponseEntity.created(URI.create("/bottaries/" + bottariId)).build();
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<Void> report(
            @PathVariable final Long id,
            final ReportBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        reportService.reportBottariTemplate(ssaid, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable final Long id,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        bottariTemplateService.deleteById(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
