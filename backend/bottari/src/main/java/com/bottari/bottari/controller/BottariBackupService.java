package com.bottari.bottari.controller;

import com.bottari.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BottariBackupService {

    private static final String BOTTARI_BACKUP_PREFIX = "bottari";

    private final S3Service s3Service;

    public String backup(
            final String ssaid,
            final MultipartFile file
    ) {
        return s3Service.upload(
                file,
                BOTTARI_BACKUP_PREFIX,
                "ssaid" + "_" + ssaid
        );
    }
}
