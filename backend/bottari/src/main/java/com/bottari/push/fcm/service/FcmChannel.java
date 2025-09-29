package com.bottari.push.fcm.service;

import static com.bottari.error.ErrorCode.FCM_INVALID_TOKEN;
import static com.bottari.error.ErrorCode.FCM_MESSAGE_SEND_FAIL;
import static com.bottari.error.ErrorCode.INVALID_PUSH_MESSAGE_TYPE;

import com.bottari.error.BusinessException;
import com.bottari.push.ChannelType;
import com.bottari.push.NotificationBasedChannel;
import com.bottari.push.PushMessage;
import com.bottari.push.fcm.domain.FcmToken;
import com.bottari.push.fcm.dto.SendMessageRequest;
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
public final class FcmChannel implements NotificationBasedChannel {

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
    public void broadcast(final PushMessage message) {
        throw new UnsupportedOperationException();
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
        if (message.channelType() != ChannelType.FCM) {
            throw new BusinessException(INVALID_PUSH_MESSAGE_TYPE, "FCM");
        }
        final SendMessageRequest request = (SendMessageRequest) message;

        return Message.builder()
                .setToken(fcmToken.getToken())
                .putData("type", request.getMessageType().name())
                .putAllData(request.getData())
                .build();
    }
}
