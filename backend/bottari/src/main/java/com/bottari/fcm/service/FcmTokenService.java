package com.bottari.fcm.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.fcm.dto.UpdateFcmRequest;
import com.bottari.fcm.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void updateFcmToken(
            final String ssaid,
            final UpdateFcmRequest request
    ) {
        final FcmToken fcmToken = fcmTokenRepository.findByMemberSsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.FCM_TOKEN_NOT_FOUND));
        fcmToken.updateFcmToken(request.fcmToken());
    }
}
