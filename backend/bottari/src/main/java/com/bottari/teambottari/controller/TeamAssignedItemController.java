package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.service.TeamAssignedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-assigned-items")
public class TeamAssignedItemController implements TeamAssignedItemApiDocs {

    private final TeamAssignedItemService teamAssignedItemService;

    @PatchMapping("/{id}/check")
    @Override
    public ResponseEntity<Void> check(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamAssignedItemService.check(id, ssaid);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamAssignedItemService.uncheck(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
