package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.service.TeamPersonalItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-personal-items")
public class TeamPersonalItemController implements TeamPersonalItemApiDocs {

    private final TeamPersonalItemService teamPersonalItemService;

    @PatchMapping("/{id}/check")
    @Override
    public ResponseEntity<Void> check(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamPersonalItemService.check(id, ssaid);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        teamPersonalItemService.uncheck(id, ssaid);

        return ResponseEntity.noContent().build();
    }
}
