package com.bottari.push.notification;

import com.bottari.push.ChannelType;
import com.bottari.push.PushChannel;
import com.bottari.push.message.PushMessage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NotificationChannels {

    private final Map<ChannelType, NotificationChannel> notificationChannels;
    private final NotificationChannelProvider notificationChannelProvider;

    public NotificationChannels(
            final List<NotificationChannel> notificationChannels,
            final NotificationChannelProvider notificationChannelProvider
    ) {
        this.notificationChannels = notificationChannels.stream()
                .collect(Collectors.toMap(
                        PushChannel::channelType,
                        channel -> channel));
        this.notificationChannelProvider = notificationChannelProvider;
    }

    public void unicast(
            final PushMessage pushMessage,
            final Long memberId
    ) {
        final ChannelType channelType = notificationChannelProvider.channel(memberId);
        final NotificationChannel channel = notificationChannels.get(channelType);
        if (channel != null) {
            channel.unicast(pushMessage, memberId);
        }
    }

    public void multicast(
            final PushMessage pushMessage,
            final List<Long> memberIds
    ) {
        final Map<ChannelType, List<Long>> channelByMembers = notificationChannelProvider.channel(memberIds);
        for (final ChannelType channelType : channelByMembers.keySet()) {
            final NotificationChannel channel = notificationChannels.get(channelType);
            if (channel != null) {
                channel.multicast(pushMessage, channelByMembers.get(channelType));
            }
        }
    }
}
