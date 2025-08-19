package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse;
import com.bottari.teambottari.service.TeamBottariService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team-bottaries")
@RequiredArgsConstructor
public class TeamBottariController implements TeamBottariApiDocs {

    private final TeamBottariService teamBottariService;

    @GetMapping
    @Override
    public ResponseEntity<List<ReadTeamBottariPreviewResponse>> readPreviews(
            @MemberIdentifier final String ssaid
    ) {
        final List<ReadTeamBottariPreviewResponse> responses = teamBottariService.getAllBySsaid(ssaid);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ReadTeamBottariResponse> read(
            @PathVariable final Long id,
            @MemberIdentifier final String ssaid
    ) {
        final ReadTeamBottariResponse response = teamBottariService.getById(ssaid, id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<Void> create(
            @RequestBody final CreateTeamBottariRequest request,
            @MemberIdentifier final String ssaid
    ) {
        final Long id = teamBottariService.create(ssaid, request);

        return ResponseEntity.created(URI.create("/team-bottaries/" + id)).build();
    }
}
