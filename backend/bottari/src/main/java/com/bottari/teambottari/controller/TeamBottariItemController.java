package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.service.TeamItemFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamBottariItemController implements TeamBottariItemApiDocs {

    private final TeamItemFacade teamItemFacade;

    @GetMapping("/team-bottaries/{teamBottariId}/checklist")
    @Override
    public ResponseEntity<TeamMemberChecklistResponse> readChecklistBySsaid(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final TeamMemberChecklistResponse response = teamItemFacade.getCheckList(teamBottariId, ssaid);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/team-items/{id}/check")
    @Override
    public ResponseEntity<Void> check(
            @PathVariable final Long id,
            @RequestBody final CheckTeamItemRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.check(id, ssaid, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/team-items/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id,
            @RequestBody final CheckTeamItemRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.uncheck(id, ssaid, request);

        return ResponseEntity.noContent().build();
    }
}
