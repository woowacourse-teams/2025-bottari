package com.bottari.bottaritemplate.controller;

import com.bottari.bottaritemplate.dto.ReadPopularHashtagResponse;
import com.bottari.bottaritemplate.service.HashtagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hashtags")
@RequiredArgsConstructor
public class HashtagController implements HashtagApiDocs {

    private final HashtagService hashtagService;

    @GetMapping("/popular")
    @Override
    public ResponseEntity<List<ReadPopularHashtagResponse>> readPopularHashtags(
            @RequestParam(defaultValue = "10") final int limit
    ) {
        final List<ReadPopularHashtagResponse> responses = hashtagService.getTopHashtagsByUsageCount(limit);

        return ResponseEntity.ok(responses);
    }
}
