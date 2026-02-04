package com.bottari.bottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.service.MemberService;
import com.bottari.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BottariBackupService {

    private static final String BOTTARI_BACKUP_PREFIX = "bottari";

    private final S3Service s3Service;
    private final MemberService memberService;

    public Resource load(final String ssaid) {
        verifySsaid(ssaid);
        try {
            return s3Service.download(
                    BOTTARI_BACKUP_PREFIX,
                    getKeyName(ssaid)
            );
        } catch (final Exception e) {
            throw new BusinessException(ErrorCode.BOTTARI_LOAD_FAILED, e.getMessage());
        }
    }

    public String backup(
            final String ssaid,
            final MultipartFile file
    ) {
        verifySsaid(ssaid);
        try {
            return s3Service.upload(
                    file,
                    BOTTARI_BACKUP_PREFIX,
                    getKeyName(ssaid)
            );
        } catch (final Exception e) {
            throw new BusinessException(ErrorCode.BOTTARI_BACKUP_FAILED, e.getMessage());
        }
    }

    private void verifySsaid(final String ssaid) {
        if (!memberService.checkRegistration(ssaid).isRegistered()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND, ssaid);
        }
    }

    private String getKeyName(final String ssaid) {
        return "ssaid" + "_" + ssaid;
    }
}
