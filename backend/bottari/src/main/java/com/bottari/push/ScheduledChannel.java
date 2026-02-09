package com.bottari.push;

import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.NotificationChannel;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledChannel extends NotificationChannel {

    void unicast(
            final PushMessage message,
            final Long memberId,
            final LocalDateTime sendAt
    );

    void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final LocalDateTime sendAt
    );
}
