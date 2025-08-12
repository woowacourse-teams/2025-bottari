package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.service.TeamBottariItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamBottariItemController implements TeamBottariItemApiDocs {

    private final TeamBottariItemService teamBottariItemService;

    @GetMapping("/teams/{teamBottariId}/checklist")
    @Override
    public ResponseEntity<TeamMemberChecklistResponse> readChecklistBySsaid(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final TeamMemberChecklistResponse response = teamBottariItemService.getCheckList(teamBottariId, ssaid);

        return ResponseEntity.ok(response);
    }
}
