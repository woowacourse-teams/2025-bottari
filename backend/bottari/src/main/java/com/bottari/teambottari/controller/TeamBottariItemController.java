package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.service.TeamItemFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamBottariItemController implements TeamBottariItemApiDocs {

    private final TeamItemFacade teamItemFacade;

    @GetMapping("/team-bottaries/{teamBottariId}/items/status")
    @Override
    public ResponseEntity<ReadTeamItemStatusResponse> readTeamItemsStatus(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final ReadTeamItemStatusResponse response = teamItemFacade.getTeamItemStatus(teamBottariId, ssaid);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/team-items/{infoId}/remind")
    @Override
    public ResponseEntity<Void> sendRemindAlarmByInfo(
            @PathVariable final Long infoId,
            @RequestBody final TeamItemTypeRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.sendRemindAlarmByInfo(infoId, request, ssaid);

        return ResponseEntity.noContent().build();
    }

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
            @RequestBody final TeamItemTypeRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.check(id, ssaid, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/team-items/{id}/uncheck")
    @Override
    public ResponseEntity<Void> uncheck(
            @PathVariable final Long id,
            @RequestBody final TeamItemTypeRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.uncheck(id, ssaid, request);

        return ResponseEntity.noContent().build();
    }
}
