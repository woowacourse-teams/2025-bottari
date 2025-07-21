package com.bottari.controller;

import com.bottari.controller.docs.MemberApiDocs;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.service.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
