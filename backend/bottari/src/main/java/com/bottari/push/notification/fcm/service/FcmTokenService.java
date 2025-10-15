package com.bottari.push.notification.fcm.service;

import static com.bottari.error.ErrorCode.FCM_TOKEN_NOT_FOUND;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.push.notification.fcm.domain.FcmToken;
import com.bottari.push.notification.fcm.dto.UpdateFcmRequest;
import com.bottari.push.notification.fcm.repository.FcmTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmToken getByMemberId(final Long memberId) {
        return fcmTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(FCM_TOKEN_NOT_FOUND));
    }

    public List<FcmToken> getByMembersIn(final List<Long> memberIds) {
        return fcmTokenRepository.findByMemberIdIn(memberIds);
    }

    @Transactional
    public void updateFcmToken(
            final String ssaid,
            final UpdateFcmRequest request
    ) {
        final FcmToken fcmToken = fcmTokenRepository.findByMemberSsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.FCM_TOKEN_NOT_FOUND));
        fcmToken.updateFcmToken(request.fcmToken());
    }

    public void deleteById(final Long id) {
        fcmTokenRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIds(final List<Long> ids) {
        fcmTokenRepository.deleteByIds(ids);
    }
}
