package com.bottari.fcm;

import static com.bottari.error.ErrorCode.FCM_INVALID_TOKEN;
import static com.bottari.error.ErrorCode.FCM_MESSAGE_SEND_FAIL;
import static com.bottari.error.ErrorCode.FCM_TOKEN_NOT_FOUND;

import com.bottari.error.BusinessException;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.fcm.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmMessageSender {

    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendMessageToMember(
            final Long memberId,
            final SendMessageRequest request
    ) {
        final FcmToken fcmToken = fcmTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(FCM_TOKEN_NOT_FOUND));
        final Message message = createMessage(request, fcmToken);
        try {
            firebaseMessaging.send(message);
        } catch (final FirebaseMessagingException e) {
            if (isInvalidFcmToken(e)) {
                fcmTokenRepository.deleteById(fcmToken.getId());
                throw new BusinessException(FCM_INVALID_TOKEN);
            }
            throw new BusinessException(FCM_MESSAGE_SEND_FAIL);
        }
    }

    public void sendMessageToMembers(
            final List<Long> memberIds,
            final SendMessageRequest request
    ) {
        final List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberIdIn(memberIds);
        final List<Long> invalidTokenIds = new ArrayList<>();
        for (final FcmToken fcmToken : fcmTokens) {
            final Message message = createMessage(request, fcmToken);
            try {
                firebaseMessaging.send(message);
            } catch (final FirebaseMessagingException e) {
                if (isInvalidFcmToken(e)) {
                    invalidTokenIds.add(fcmToken.getId());
                    continue;
                }
                throw new BusinessException(FCM_MESSAGE_SEND_FAIL);
            }
        }
        if (!invalidTokenIds.isEmpty()) {
            fcmTokenRepository.deleteByIds(invalidTokenIds);
            throw new BusinessException(FCM_INVALID_TOKEN);
        }
    }

    private boolean isInvalidFcmToken(final FirebaseMessagingException exception) {
        final MessagingErrorCode messagingErrorCode = exception.getMessagingErrorCode();

        return messagingErrorCode == MessagingErrorCode.UNREGISTERED
                || messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT;
    }

    private Message createMessage(
            final SendMessageRequest request,
            final FcmToken fcmToken
    ) {
        return Message.builder()
                .setToken(fcmToken.getToken())
                .setNotification(request.createNotification())
                .putData("type", request.messageType().name())
                .build();
    }
}
