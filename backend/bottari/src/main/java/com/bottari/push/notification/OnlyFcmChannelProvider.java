package com.bottari.push.notification;

import com.bottari.push.ChannelType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OnlyFcmChannelProvider implements NotificationChannelProvider {

    @Override
    public ChannelType channel(final Long memberId) {
        return ChannelType.FCM;
    }

    @Override
    public Map<ChannelType, List<Long>> channel(final List<Long> memberIds) {
        return Map.of(ChannelType.FCM, memberIds);
    }
}
