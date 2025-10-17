package com.bottari.push.connection.sse.external;

public record RedisTopic(
        String name
) {

    private static final String TOPIC_NAME_PREFIX = "member:";

    public static RedisTopic from(final Long memberId) {
        return new RedisTopic(TOPIC_NAME_PREFIX + memberId);
    }

    public Long extractMemberId() {
        return Long.valueOf(name.substring(TOPIC_NAME_PREFIX.length()));
    }
}
