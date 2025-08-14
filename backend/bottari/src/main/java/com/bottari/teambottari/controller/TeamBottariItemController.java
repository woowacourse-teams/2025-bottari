package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.service.TeamItemFacade;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/team-bottaries/{teamBottariId}/shared-items")
    @Override
    public ResponseEntity<Void> createShared(
            @PathVariable final Long teamBottariId,
            @RequestBody final CreateTeamItemRequest request,
            @MemberIdentifier final String ssaid
    ) {
        final Long id = teamItemFacade.createSharedItem(teamBottariId, request, ssaid);
        final URI location = URI.create("/team-bottaries/" + teamBottariId + "/shared-items/" + id);

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/team-bottaries/{teamBottariId}/personal-items")
    @Override
    public ResponseEntity<Void> createPersonal(
            @PathVariable final Long teamBottariId,
            @RequestBody final CreateTeamItemRequest request,
            @MemberIdentifier final String ssaid
    ) {
        final Long id = teamItemFacade.createPersonalItem(teamBottariId, request, ssaid);
        final URI location = URI.create("/team-bottaries/" + teamBottariId + "/personal-items/" + id);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/team-items/{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable final Long id,
            @RequestBody final TeamItemTypeRequest request,
            @MemberIdentifier final String ssaid
    ) {
        teamItemFacade.delete(id, ssaid, request);

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

    @GetMapping("/team-bottaries/{teamBottariId}/items/status")
    @Override
    public ResponseEntity<ReadTeamItemStatusResponse> readTeamItemsStatus(
            @PathVariable final Long teamBottariId,
            @MemberIdentifier final String ssaid
    ) {
        final ReadTeamItemStatusResponse response = teamItemFacade.getTeamItemStatus(teamBottariId, ssaid);

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
