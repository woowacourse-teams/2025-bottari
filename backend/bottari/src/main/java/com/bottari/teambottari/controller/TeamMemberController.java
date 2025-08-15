package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberNameResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.service.TeamMemberService;
import java.util.List;
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

    @GetMapping("/team-bottaries/{teamBottariId}/members/name")
    public ResponseEntity<List<ReadTeamMemberNameResponse>> readTeamMemberNameInfo(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final List<ReadTeamMemberNameResponse> response = teamMemberService.getTeamMemberNameByTeamBottariId(
                teamBottariId,
                ssaid
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/team-bottaries/{teamBottariId}/members/status")
    @Override
    public ResponseEntity<List<ReadTeamMemberStatusResponse>> readTeamMemberStatus(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final List<ReadTeamMemberStatusResponse> response = teamMemberService.getTeamMemberStatusByTeamBottariId(
                teamBottariId,
                ssaid
        );

        return ResponseEntity.ok(response);
    }
}
