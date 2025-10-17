package com.bottari.push.connection.sse.external;

import org.springframework.data.redis.listener.ChannelTopic;

public final class MemberChannelTopic extends ChannelTopic {

    private static final String TOPIC_NAME_PREFIX = "member:";

    public MemberChannelTopic(final String name) {
        super(name);
    }

    public static MemberChannelTopic from(final Long memberId) {
        return new MemberChannelTopic(TOPIC_NAME_PREFIX + memberId);
    }

    public Long extractMemberId() {
        return Long.valueOf(getTopic().substring(TOPIC_NAME_PREFIX.length()));
    }
}
