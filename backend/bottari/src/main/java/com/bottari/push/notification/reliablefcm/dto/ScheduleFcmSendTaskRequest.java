package com.bottari.push.notification.reliablefcm.dto;

import com.bottari.push.message.PushMessage;
import java.time.LocalDateTime;

public record ScheduleFcmSendTaskRequest(
        LocalDateTime scheduledAt,
        PushMessage message,
        Long targetMemberId
) {
}
