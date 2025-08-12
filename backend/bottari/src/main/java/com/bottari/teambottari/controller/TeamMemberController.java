package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamMemberController implements TeamMemberApiDocs {

    private final TeamMemberService teamMemberService;

    @GetMapping("/team-bottaries/{teamBottariId}/members")
    @Override
    public ResponseEntity<ReadTeamMemberInfoResponse> readTeamMemberManagementInfo(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final ReadTeamMemberInfoResponse response = teamMemberService.getTeamMemberInfoByTeamBottariId(
                teamBottariId,
                ssaid
        );

        return ResponseEntity.ok(response);
    }
}
