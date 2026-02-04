package com.bottari.bottari.controller;

import com.bottari.s3.S3Service;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BottariBackupService {

    private final S3Service s3Service;

    public String backup(
            final String ssaid,
            final MultipartFile file
    ) {
        return s3Service.upload(
                file,
                "bottari/" + ssaid,
                LocalDateTime.now() + "_" + file.getOriginalFilename()
        );
    }
}
