package com.bottari.push.connection;

import com.bottari.push.ChannelType;
import com.bottari.push.PushChannel;
import com.bottari.push.message.PushMessage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ConnectionChannels {

    private final Map<ChannelType, ConnectionChannel> connectionChannels;

    public ConnectionChannels(final List<ConnectionChannel> connectionChannels) {
        this.connectionChannels = connectionChannels.stream()
                .collect(Collectors.toMap(
                        PushChannel::channelType,
                        channel -> channel));
    }

    public void unicast(
            final PushMessage pushMessage,
            final ChannelType channelType,
            final Long memberId
    ) {
        final ConnectionChannel connectionChannel = connectionChannels.get(channelType);
        if (connectionChannel != null) {
            connectionChannel.unicast(pushMessage, memberId);
        }
    }

    public void multicast(
            final PushMessage pushMessage,
            final ChannelType channelType,
            final List<Long> memberIds
    ) {
        final ConnectionChannel connectionChannel = connectionChannels.get(channelType);
        if (connectionChannel != null) {
            connectionChannel.multicast(pushMessage, memberIds);
        }
    }

    public void broadcast(
            final PushMessage pushMessage,
            final ChannelType channelType
    ) {
        final ConnectionChannel connectionChannel = connectionChannels.get(channelType);
        if (connectionChannel != null) {
            connectionChannel.broadcast(pushMessage);
        }
    }
}
