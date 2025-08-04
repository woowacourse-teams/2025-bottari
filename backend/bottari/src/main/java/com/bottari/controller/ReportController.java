package com.bottari.controller;

import com.bottari.controller.docs.ReportApiDocs;
import com.bottari.dto.ReportBottariTemplateRequest;
import com.bottari.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController implements ReportApiDocs {

    private final ReportService reportService;

    @PostMapping("/templates/{bottariTemplateId}")
    @Override
    public ResponseEntity<Void> reportBottariTemplate(
            @PathVariable final Long bottariTemplateId,
            final ReportBottariTemplateRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        reportService.reportBottariTemplate(ssaid, bottariTemplateId, request);

        return ResponseEntity.ok().build();
    }
}
