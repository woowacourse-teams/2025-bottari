package com.bottari.push;

import com.bottari.push.message.PushMessage;
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

    ChannelType channelType();
}
