package com.bottari.controller;

import com.bottari.controller.docs.BottariTemplateApiDocs;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.service.BottariTemplateService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    @Override
    public ResponseEntity<List<ReadBottariTemplateResponse>> readAll(
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getAll(query);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ReadBottariTemplateResponse> read(
            @PathVariable final Long id
    ) {
        final ReadBottariTemplateResponse response = bottariTemplateService.getById(id);

        return ResponseEntity.ok(response);
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
}
