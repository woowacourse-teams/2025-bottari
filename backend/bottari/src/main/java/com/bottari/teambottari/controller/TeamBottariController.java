package com.bottari.teambottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.service.TeamBottariService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team-bottaries")
@RequiredArgsConstructor
public class TeamBottariController implements TeamBottariApiDocs {

    private final TeamBottariService teamBottariService;

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
