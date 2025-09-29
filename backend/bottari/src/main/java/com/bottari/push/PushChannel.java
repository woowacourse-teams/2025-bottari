package com.bottari.push;

import java.util.List;

public interface PushChannel {

    void unicast(
            final PushMessage message,
            final Long memberId
    );

    void multicast(
            final PushMessage message,
            final List<Long> memberIds
    );

    void broadcast(final PushMessage message);

    ChannelType channelType();
}
