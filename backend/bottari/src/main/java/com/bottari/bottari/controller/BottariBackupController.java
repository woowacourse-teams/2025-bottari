package com.bottari.bottari.controller;

import com.bottari.bottari.dto.BackupBottariResponse;
import com.bottari.bottari.service.BottariBackupService;
import com.bottari.config.MemberIdentifier;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/load")
    @Override
    public ResponseEntity<Resource> load(
            @MemberIdentifier final String ssaid
    ) {
        final Resource resource = bottariBackupService.load(ssaid);

        return ResponseEntity.ok(resource);
    }

    @PostMapping(value = "/backup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<BackupBottariResponse> backup(
            @RequestPart("file") final MultipartFile file,
            @MemberIdentifier final String ssaid
    ) {
        final BackupBottariResponse response = bottariBackupService.backup(ssaid, file);

        return ResponseEntity.ok(response);
    }
}
