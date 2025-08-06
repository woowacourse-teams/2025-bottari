package com.bottari.report.controller;

import com.bottari.bottaritemplate.dto.ReportBottariTemplateRequest;
import com.bottari.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportApiDocs {

    private final ReportService reportService;

    @PostMapping("/templates/{bottariTemplateId}")
    @Override
    public ResponseEntity<Void> reportBottariTemplate(
            @PathVariable final Long bottariTemplateId,
            @RequestBody final ReportBottariTemplateRequest request,
            @RequestHeader("ssaid") final String ssaid
    ) {
        reportService.reportBottariTemplate(ssaid, bottariTemplateId, request);

        return ResponseEntity.ok().build();
    }
}
