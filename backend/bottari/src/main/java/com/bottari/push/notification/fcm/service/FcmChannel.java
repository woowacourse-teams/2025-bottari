package com.bottari.push.notification.fcm.service;

import static com.bottari.error.ErrorCode.FCM_INVALID_TOKEN;
import static com.bottari.error.ErrorCode.FCM_MESSAGE_CONVERT_FAIL;
import static com.bottari.error.ErrorCode.FCM_MESSAGE_SEND_FAIL;

import com.bottari.error.BusinessException;
import com.bottari.push.ChannelType;
import com.bottari.push.notification.NotificationChannel;
import com.bottari.push.notification.fcm.domain.FcmToken;
import com.bottari.push.message.PushMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public final class FcmChannel implements NotificationChannel {

    private final ObjectMapper objectMapper;
    private final FcmTokenService fcmTokenService;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {
        final FcmToken fcmToken = fcmTokenService.getByMemberId(memberId);
        final Message fcmMessage = createMessage(message, fcmToken);
        try {
            firebaseMessaging.send(fcmMessage);
        } catch (final FirebaseMessagingException e) {
            if (isInvalidFcmToken(e)) {
                fcmTokenService.deleteById(fcmToken.getId());
                throw new BusinessException(FCM_INVALID_TOKEN);
            }
            throw new BusinessException(FCM_MESSAGE_SEND_FAIL);
        }
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        final List<FcmToken> fcmTokens = fcmTokenService.getByMembersIn(memberIds);
        final List<Long> invalidTokenIds = new ArrayList<>();
        for (final FcmToken fcmToken : fcmTokens) {
            final Message fcmMessage = createMessage(message, fcmToken);
            try {
                firebaseMessaging.send(fcmMessage);
            } catch (final FirebaseMessagingException e) {
                if (isInvalidFcmToken(e)) {
                    invalidTokenIds.add(fcmToken.getId());
                    continue;
                }
                throw new BusinessException(FCM_MESSAGE_SEND_FAIL);
            }
        }
        if (!invalidTokenIds.isEmpty()) {
            fcmTokenService.deleteByIds(invalidTokenIds);
            throw new BusinessException(FCM_INVALID_TOKEN);
        }
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.FCM;
    }

    private boolean isInvalidFcmToken(final FirebaseMessagingException exception) {
        final MessagingErrorCode messagingErrorCode = exception.getMessagingErrorCode();

        return messagingErrorCode == MessagingErrorCode.UNREGISTERED
               || messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT;
    }

    private Message createMessage(
            final PushMessage message,
            final FcmToken fcmToken
    ) {
        try {
            final String data = objectMapper.writeValueAsString(message.getData());

            return Message.builder()
                    .setToken(fcmToken.getToken())
                    .putData("resource", message.getResource())
                    .putData("event", message.getEvent())
                    .putData("data", data)
                    .build();
        } catch (final JsonProcessingException e) {
            throw new BusinessException(FCM_MESSAGE_CONVERT_FAIL);
        }
    }
}
