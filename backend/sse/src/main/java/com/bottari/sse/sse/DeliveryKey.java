package com.bottari.sse.sse;

import java.util.Map;

public record DeliveryKey(
        Long memberId,
        String resource,
        String event
) {

    public static DeliveryKey from(final MemberChannelTopic topic, final PushMessage message) {
        final Long memberId = topic.extractMemberId();

        final Map<String, Object> data = (Map<String, Object>) message.data();
        final String teamBottariId = (String) data.get("teamBottariId");

        final String resource = message.resource();

        return new DeliveryKey(memberId, resource, teamBottariId);
    }
}
