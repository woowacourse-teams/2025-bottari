package com.bottari.controller;

import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.service.BottariTemplateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class BottariTemplateController {

    private final BottariTemplateService bottariTemplateService;

    @GetMapping
    public ResponseEntity<List<ReadBottariTemplateResponse>> readAll() {
        final List<ReadBottariTemplateResponse> responses = bottariTemplateService.getAll();

        return ResponseEntity.ok(responses);
    }
}
