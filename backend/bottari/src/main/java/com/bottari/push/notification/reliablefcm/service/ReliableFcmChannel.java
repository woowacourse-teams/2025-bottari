package com.bottari.push.notification.reliablefcm.service;

import com.bottari.push.ChannelType;
import com.bottari.push.ScheduledChannel;
import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.NotificationChannel;
import com.bottari.push.notification.reliablefcm.dto.ScheduleFcmSendTaskRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReliableFcmChannel implements NotificationChannel, ScheduledChannel {

    private final FcmSendTaskService fcmSendTaskService;

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {
        unicast(message, memberId, LocalDateTime.now());
    }

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId,
            final LocalDateTime sendAt
    ) {
        final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                sendAt,
                message,
                memberId
        );
        fcmSendTaskService.scheduleFcmSendTask(request);
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        multicast(message, memberIds, LocalDateTime.now());
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final LocalDateTime sendAt
    ) {
        final List<ScheduleFcmSendTaskRequest> request = memberIds.stream()
                .map(memberId -> new ScheduleFcmSendTaskRequest(
                        sendAt,
                        message,
                        memberId
                ))
                .toList();
        fcmSendTaskService.scheduleFcmSendTasks(request);
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.RELIABLE_FCM;
    }
}
