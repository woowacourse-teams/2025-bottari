package com.bottari.bottari.controller;

import com.bottari.config.MemberIdentifier;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.service.MemberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bottaries")
@RequiredArgsConstructor
public class BottariBackupController implements BottariBackupApiDocs {

    private final BottariBackupService bottariBackupService;
    private final MemberService memberService;

    @PostMapping(value = "/backup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<?, ?>> backup(
            @RequestPart("file") final MultipartFile file,
            @MemberIdentifier final String ssaid
    ) {
        if (!memberService.checkRegistration(ssaid).isRegistered()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND, ssaid);
        }
        final String key = bottariBackupService.backup(ssaid, file);

        return ResponseEntity.ok(Map.of("key", key));
    }
}
