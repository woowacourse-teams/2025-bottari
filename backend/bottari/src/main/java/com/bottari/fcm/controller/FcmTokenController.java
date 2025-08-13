package com.bottari.fcm.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.fcm.dto.UpdateFcmRequest;
import com.bottari.fcm.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class FcmTokenController implements FcmTokenApiDocs {

    private final FcmTokenService fcmTokenService;

    @PatchMapping("/fcm")
    public ResponseEntity<Void> updateFcmToken(
            @RequestBody final UpdateFcmRequest request,
            @MemberIdentifier final String ssaid
    ) {
        fcmTokenService.updateFcmToken(ssaid, request);

        return ResponseEntity.noContent().build();
    }
}
