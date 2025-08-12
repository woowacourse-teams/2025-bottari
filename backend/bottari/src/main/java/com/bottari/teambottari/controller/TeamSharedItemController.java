package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.service.TeamSharedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-shared-items")
public class TeamSharedItemController implements TeamSharedItemApiDocs {

    private final TeamSharedItemService teamSharedItemService;

    @PatchMapping("/{id}/check")
    @Override
    public ResponseEntity<Void> check(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamSharedItemService.check(id, ssaid);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamSharedItemService.uncheck(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
