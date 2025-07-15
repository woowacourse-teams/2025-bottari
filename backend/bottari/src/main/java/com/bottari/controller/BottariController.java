package com.bottari.controller;

import com.bottari.dto.CreateBottariRequest;
import com.bottari.service.BottariService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bottaries")
@RequiredArgsConstructor
public class BottariController {

    private final BottariService bottariService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody final CreateBottariRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        final Long id = bottariService.create(ssaid, request);

        return ResponseEntity.created(URI.create("/bottaries/" + id)).build();
    }
}
