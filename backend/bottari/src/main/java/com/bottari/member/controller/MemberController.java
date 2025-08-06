package com.bottari.member.controller;

import com.bottari.member.service.MemberService;
import com.bottari.member.dto.CheckRegistrationResponse;
import com.bottari.member.dto.CreateMemberRequest;
import com.bottari.member.dto.UpdateMemberRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @PostMapping
    @Override
    public ResponseEntity<Void> register(
            @RequestBody final CreateMemberRequest request
    ) {
        final Long id = memberService.create(request);

        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/check")
    @Override
    public ResponseEntity<CheckRegistrationResponse> checkRegistration(
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        final CheckRegistrationResponse response = memberService.checkRegistration(ssaid);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    @Override
    public ResponseEntity<Void> updateName(
            @RequestBody final UpdateMemberRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        final String ssaid = httpServletRequest.getHeader("ssaid");
        memberService.updateName(ssaid, request);

        return ResponseEntity.noContent().build();
    }
}
