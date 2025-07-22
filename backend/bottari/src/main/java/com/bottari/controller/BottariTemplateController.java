package com.bottari.controller;

import com.bottari.controller.docs.BottariTemplateApiDocs;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.service.BottariTemplateService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class BottariTemplateController implements BottariTemplateApiDocs {

    private final BottariTemplateService bottariTemplateService;

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
}
