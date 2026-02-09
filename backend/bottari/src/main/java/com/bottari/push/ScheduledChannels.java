package com.bottari.push;

import com.bottari.push.message.PushMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ScheduledChannels {

    private final Map<ChannelType, ScheduledChannel> scheduledChannels;

    public ScheduledChannels(final List<ScheduledChannel> scheduledChannels) {
        this.scheduledChannels = scheduledChannels.stream()
                .collect(Collectors.toMap(
                        PushChannel::channelType,
                        channel -> channel));
    }

    public void unicast(
            final PushMessage pushMessage,
            final ChannelType channelType,
            final Long memberId,
            final LocalDateTime sendAt
    ) {
        final ScheduledChannel channel = scheduledChannels.get(channelType);
        if (channel != null) {
            channel.unicast(pushMessage, memberId, sendAt);
        }
    }

    public void multicast(
            final PushMessage pushMessage,
            final ChannelType channelType,
            final List<Long> memberIds,
            final LocalDateTime sendAt
    ) {
        final ScheduledChannel channel = scheduledChannels.get(channelType);
        if (channel != null) {
            channel.multicast(pushMessage, memberIds, sendAt);
        }
    }
}
