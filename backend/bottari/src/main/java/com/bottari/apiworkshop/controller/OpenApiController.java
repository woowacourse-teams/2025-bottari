package com.bottari.apiworkshop.controller;

import com.bottari.apiworkshop.dto.CursorRequest;
import com.bottari.apiworkshop.dto.MostIncludedQuery;
import com.bottari.apiworkshop.dto.MostIncludedResponse;
import com.bottari.apiworkshop.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

    private final OpenApiService openApiService;

    @GetMapping("/templates/most-included")
    public ResponseEntity<MostIncludedResponse> mostIncluded(
            @ModelAttribute final MostIncludedQuery query,
            @ModelAttribute final CursorRequest cursor
    ) {
        final MostIncludedResponse response = openApiService.mostIncluded(query, cursor);

        return ResponseEntity.ok(response);
    }
}
