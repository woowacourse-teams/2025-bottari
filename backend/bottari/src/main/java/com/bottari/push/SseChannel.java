package com.bottari.push;

import java.util.List;

public final class SseChannel implements ConnectionBasedChannel {

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {

    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {

    }

    @Override
    public void broadcast(final PushMessage message) {

    }

    @Override
    public ChannelType channelType() {
        return ChannelType.SSE;
    }
}
