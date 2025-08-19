package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.JoinTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberNameResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.service.TeamMemberService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Override
    public ResponseEntity<List<ReadTeamMemberNameResponse>> readTeamMemberNames(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final List<ReadTeamMemberNameResponse> response = teamMemberService.getTeamMemberNamesByTeamBottariId(
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

    @PostMapping("/team-bottaries/join")
    @Override
    public ResponseEntity<Void> joinTeamBottari(
            @RequestBody final JoinTeamBottariRequest request,
            @MemberIdentifier final String ssaid
    ) {
        final Long id = teamMemberService.joinTeamBottari(request, ssaid);

        return ResponseEntity.created(URI.create("/team-members/" + id)).build();
    }

    @PostMapping("/team-bottaries/{teamBottariId}/members/{memberId}/remind")
    @Override
    public ResponseEntity<Void> sendRemindAlarmByMember(
            @PathVariable final Long teamBottariId,
            @PathVariable final Long memberId,
            @MemberIdentifier final String ssaid
    ) {
        teamMemberService.sendRemindAlarm(teamBottariId, memberId, ssaid);

        return ResponseEntity.noContent().build();
    }
}
